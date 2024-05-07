package musicboxd.android.ui.details

import android.graphics.BitmapFactory
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import musicboxd.android.LAST_FM_API_KEY
import musicboxd.android.data.remote.api.lastfm.LastFMAPIRepo
import musicboxd.android.data.remote.api.songlink.SongLinkRepo
import musicboxd.android.data.remote.api.spotify.SpotifyAPIRepo
import musicboxd.android.data.remote.api.spotify.model.album.Albums
import musicboxd.android.data.remote.api.spotify.model.artist_search.ExternalUrls
import musicboxd.android.data.remote.api.spotify.model.artist_search.Followers
import musicboxd.android.data.remote.api.spotify.model.artist_search.Item
import musicboxd.android.data.remote.api.spotify.model.topTracks.TopTracksDTO
import musicboxd.android.ui.details.album.AlbumDetailScreenState
import musicboxd.android.ui.details.model.ItemExternalLink
import musicboxd.android.ui.search.SearchScreenViewModel
import org.jsoup.Jsoup
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val spotifyAPIRepo: SpotifyAPIRepo,
    private val songLinkRepo: SongLinkRepo,
    private val lastFMAPIRepo: LastFMAPIRepo
) : SearchScreenViewModel(spotifyAPIRepo) {
    var albumScreenState = AlbumDetailScreenState(
        covertArtImgUrl = flow { },
        albumImgUrl = "",
        albumTitle = "",
        artists = listOf(),
        albumWiki = flow { },
        releaseDate = "",
        trackList = flow { }, artistId = "", itemType = ""
    )
    var previewCardColor = mutableStateOf(Color.Black)
    var paletteColors = mutableStateOf<Palette?>(null)
    val canvasUrl = mutableStateOf(emptyList<String>())
    val albumExternalLinks = mutableStateOf(emptyList<ItemExternalLink>())
    private val _artistAlbums =
        MutableStateFlow(Albums(items = listOf(), limit = 0, offset = 0, total = 0))
    val artistAlbums = _artistAlbums.asStateFlow()
    private val _artistWiki = MutableStateFlow("")
    val artistBio = _artistWiki.asStateFlow()
    val artistSocials = mutableStateOf<List<String>>(emptyList())
    val artistInfo = mutableStateOf(
        Item(
            external_urls = ExternalUrls(
                spotify = "",
            ),
            followers = Followers(
                total = 0,
            ),
            genres = listOf(),
            href = "",
            id = "",
            images = listOf(),
            name = "",
            popularity = 0,
            type = "",
            uri = ""
        )
    )
    private val _topTracksDTO = MutableStateFlow(TopTracksDTO(listOf()))
    val topTracksDTO = _topTracksDTO.asStateFlow()
    private val _lastFMImage = MutableStateFlow("")
    val lastFMImage = _lastFMImage.asStateFlow()
    fun loadAlbumInfo(
        artistID: String,
        albumName: String,
        albumID: String,
        artistName: String,
        loadArtistImg: Boolean = true
    ) {
        albumScreenState = albumScreenState.copy(artistId = artistID)
        canvasUrl.value = emptyList()
        albumExternalLinks.value = emptyList()
        spotifyToken?.accessToken?.let {
            viewModelScope.launch {
                awaitAll(async {
                    albumScreenState = albumScreenState.copy(albumWiki = flow {
                        emit(
                            lastFMAPIRepo.getAlbumInfo(
                                artistName,
                                albumName,
                                LAST_FM_API_KEY
                            ).album.wiki.content
                        )
                    })
                }, async {
                    if (loadArtistImg) {
                        albumScreenState = albumScreenState.copy(covertArtImgUrl = flow {
                            emit(spotifyAPIRepo.getArtistData(artistID, it).images.first().url)
                        })
                    }
                    val inputStream = withContext(Dispatchers.IO) {
                        URL(albumScreenState.albumImgUrl).openConnection().getInputStream()
                    }
                    val albumCoverArtBitmap = BitmapFactory.decodeStream(inputStream)
                    val paletteForAlbumCoverArt = Palette.from(albumCoverArtBitmap).generate()
                    paletteColors.value = paletteForAlbumCoverArt
                    previewCardColor.value =
                        Color(paletteForAlbumCoverArt.getDarkMutedColor(Color.Black.toArgb()))
                }, async {
                    albumScreenState = albumScreenState.copy(trackList = flow {
                        emit(
                            spotifyAPIRepo.getTrackListOfAnAlbum(
                                authorizationToken = it, albumID = albumID
                            ).items
                        )
                    })
                }, async {
                    loadExternalLinks(false, "", albumID)
                })
            }.invokeOnCompletion {
                loadCanvases()
            }
        }
    }

    fun loadArtistInfo(
        artistID: String,
        artistName: String,
        navigatingFromAlbumScreen: Boolean = false
    ) {
        artistSocials.value = emptyList()
        viewModelScope.launch {
            _lastFMImage.emit("")
        }
        _topTracksDTO.value = TopTracksDTO(tracks = listOf())
        _artistAlbums.value = Albums(items = listOf(), limit = 0, offset = 0, total = 0)
        viewModelScope.launch {
            spotifyToken?.let {
                awaitAll(async {
                    if (navigatingFromAlbumScreen) {
                        loadArtistMetaData(artistID)
                    }
                }, async {
                    _topTracksDTO.emit(
                        spotifyAPIRepo.getTopTracksOfAnArtist(
                            artistID, it.accessToken
                        )
                    )
                }, async {
                    _artistWiki.emit(
                        lastFMAPIRepo.getArtistInfo(
                            artistName,
                            LAST_FM_API_KEY
                        ).artist.bio.content.substringBefore("<a href=")
                    )
                }, async {
                    _artistAlbums.emit(
                        spotifyAPIRepo.getAlbumsOfAnArtist(
                            artistID, it.accessToken
                        )
                    )
                }, async {
                    val socialsItem = withContext(Dispatchers.IO) {
                        Jsoup.connect("https://open.spotify.com/artist/$artistID")
                            .userAgent("Mozilla")
                            .header("Accept", "text/html")
                            .header("Accept-Encoding", "gzip,deflate")
                            .header(
                                "Accept-Language",
                                "it-IT,en;q=0.8,en-US;q=0.6,de;q=0.4,it;q=0.2,es;q=0.2"
                            )
                            .header("Connection", "keep-alive")
                            .ignoreContentType(true).get()
                    }.toString().substringAfter("<div class=\"ZdmJvgeayszd6ZvSl5B6\">")
                        .substringBefore("<div class=\"iQxdxLc2HsEnJMZt0Us4\">")
                        .split("<li class=\"p44AskfOeVB1s75duZoC\"><a rel=\"noopener noreferrer\" target=\"_blank\" href=\"")
                        .map {
                            it.substringAfter("https://").substringBefore("\" class")
                        }.filter { it.contains(".com") }.map { "https://".plus(it) }
                    artistSocials.value = socialsItem
                }, async {
                    val imageItem = withContext(Dispatchers.IO) {
                        Jsoup.connect(
                            "https://www.last.fm/music/${
                                artistName.replace(
                                    " ",
                                    "+"
                                )
                            }/+images"
                        ).userAgent("Mozilla")
                            .header("Accept", "text/html")
                            .header("Accept-Encoding", "gzip,deflate")
                            .header(
                                "Accept-Language",
                                "it-IT,en;q=0.8,en-US;q=0.6,de;q=0.4,it;q=0.2,es;q=0.2"
                            )
                            .header("Connection", "keep-alive")
                            .ignoreContentType(true).get()
                    }.toString().substringAfter("<a href=\"/music/").substringAfter("+images/")
                        .substringBefore("\" class")
                    val imgURL = "https://lastfm.freetls.fastly.net" + withContext(Dispatchers.IO) {
                        Jsoup.connect(
                            "https://www.last.fm/music/${
                                artistName.replace(
                                    " ",
                                    "+"
                                )
                            }/+images/${imageItem.substringBefore("\">")}".replace(" ", "+")
                        ).userAgent("Mozilla")
                            .header("Accept", "text/html")
                            .header("Accept-Encoding", "gzip,deflate")
                            .header(
                                "Accept-Language",
                                "it-IT,en;q=0.8,en-US;q=0.6,de;q=0.4,it;q=0.2,es;q=0.2"
                            )
                            .header("Connection", "keep-alive")
                            .ignoreContentType(true)
                            .get()
                    }.toString().substringAfter("src=\"https://lastfm.freetls.fastly.net")
                        .substringBefore("\">")
                    _lastFMImage.emit(imgURL)
                })
            }
        }
    }

    fun loadArtistMetaData(artistID: String) {
        viewModelScope.launch {
            spotifyToken?.let {
                val artistData = spotifyAPIRepo.getArtistData(artistID, it.accessToken)
                albumScreenState =
                    albumScreenState.copy(covertArtImgUrl = flowOf(artistData.images.first().url))
                artistInfo.value = Item(
                    external_urls = artistData.external_urls,
                    followers = artistData.followers,
                    genres = artistData.genres,
                    href = "",
                    id = artistData.id,
                    images = artistData.images,
                    name = artistData.name,
                    popularity = artistData.popularity,
                    type = artistData.type,
                    uri = artistData.uri
                )
            }
        }
    }

    fun loadExternalLinks(isTrack: Boolean, trackID: String, albumID: String) {
        albumExternalLinks.value = emptyList()
        viewModelScope.launch {
            val itemType = if (isTrack) "track" else "album"
            val itemId = if (isTrack) trackID else albumID
            val albumExternalLinksAPIData =
                songLinkRepo.getLinks("https://open.spotify.com/$itemType/$itemId").linksByPlatform
            albumExternalLinks.value = listOf(
                ItemExternalLink(
                    imgURL = "https://play-lh.googleusercontent.com/WKVNPckIxFKWO22Cu1o7zZf8l36-caVIkneZD7ocSBgGbSWYbnkyE4iECGI_qEhHkuQ",
                    externalLink = albumExternalLinksAPIData.amazonMusic.url
                ),
                ItemExternalLink(
                    imgURL = "https://asset.brandfetch.io/id4G4LfCWL/idAXHZ3WLJ.jpeg",
                    externalLink = albumExternalLinksAPIData.anghami.url
                ),
                ItemExternalLink(
                    imgURL = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5f/Apple_Music_icon.svg/2048px-Apple_Music_icon.svg.png",
                    externalLink = albumExternalLinksAPIData.appleMusic.url
                ),
                ItemExternalLink(
                    imgURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRoAajHlXLDtUtidNRDFDlCOidk_mXIbeLk4qtds_tuap4699CnsHylLd9rjEz9JKqtAPc&usqp=CAU",
                    externalLink = albumExternalLinksAPIData.audiomack.url
                ),
                ItemExternalLink(
                    imgURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQnPbBTXM_f1seJ-qjOxEkRLJQ1c6sbjkAyIQFKv2Am_Q&s",
                    externalLink = albumExternalLinksAPIData.boomplay.url
                ),
                ItemExternalLink(
                    imgURL = "https://store-images.s-microsoft.com/image/apps.46581.13510798886741797.6272719c-6e87-457e-89fa-c3c223a1c07a.f8b3708e-1ddf-4cb3-b201-9263bf813aae?h=210",
                    externalLink = albumExternalLinksAPIData.deezer.url
                ),
                ItemExternalLink(
                    imgURL = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/df/ITunes_logo.svg/438px-ITunes_logo.svg.png",
                    externalLink = albumExternalLinksAPIData.itunes.url
                ),
                ItemExternalLink(
                    imgURL = "https://images.squarespace-cdn.com/content/v1/55915377e4b05e44b9c13bca/1535515150211-O2O5767XRDJRF1WG2A1Q/Napster-Logo-.png?format=2500w",
                    externalLink = albumExternalLinksAPIData.napster.url
                ),
                ItemExternalLink(
                    imgURL = "https://store-images.s-microsoft.com/image/apps.31523.9007199266244713.666a3fe2-1a3e-4c9f-8301-d7fb23270871.7fd63e6c-7ca0-4e8a-9eed-ceee957307f2?h=210",
                    externalLink = albumExternalLinksAPIData.pandora.url
                ),
                ItemExternalLink(
                    imgURL = "https://store-images.s-microsoft.com/image/apps.22696.14398308773733109.53eed167-276f-443c-a7a5-c0d635242775.283cc376-189e-4777-8a82-29f500a9de4f?h=210",
                    externalLink = albumExternalLinksAPIData.soundcloud.url
                ),
                ItemExternalLink(
                    imgURL = "https://store-images.s-microsoft.com/image/apps.10546.13571498826857201.6603a5e2-631f-4f29-9b08-f96589723808.dc893fe0-ecbc-4846-9ac6-b13886604095?h=210",
                    externalLink = albumExternalLinksAPIData.spotify.url
                ),
                ItemExternalLink(
                    imgURL = "https://store-images.s-microsoft.com/image/apps.54003.13740903646068838.738514b4-7fb6-4210-af7f-9aa318dd0a71.d958b4a6-53ef-42fc-96c5-08796bbcb894?h=210",
                    externalLink = albumExternalLinksAPIData.tidal.url
                ),
                ItemExternalLink(
                    imgURL = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e7/Yandex_Music_icon.svg/165px-Yandex_Music_icon.svg.png",
                    externalLink = albumExternalLinksAPIData.yandex.url
                ),
                ItemExternalLink(
                    imgURL = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/09/YouTube_full-color_icon_%282017%29.svg/512px-YouTube_full-color_icon_%282017%29.svg.png?20240107144800",
                    externalLink = albumExternalLinksAPIData.youtube.url
                ),
                ItemExternalLink(
                    imgURL = "https://play-lh.googleusercontent.com/GnYnNfKBr2nysHBYgYRCQtcv_RRNN0Sosn47F5ArKJu89DMR3_jHRAazoIVsPUoaMg=w240-h480",
                    externalLink = albumExternalLinksAPIData.youtubeMusic.url
                ),
            )
        }
    }

    fun loadCanvases() {
        canvasUrl.value = emptyList()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                albumScreenState.trackList.first().forEach {
                    val rawHtml =
                        Jsoup.connect("https://www.canvasdownloader.com/canvas?link=https://open.spotify.com/track/${it.id}")
                            .get().toString()
                    if (rawHtml.contains("<source src=\"https://")) {
                        rawHtml.substringAfter("<source src=\"https://")
                            .substringBefore(".mp4\" type=\"video/mp4\">").let { url ->
                                canvasUrl.value += ("https://$url.mp4")
                            }
                    } else {
                        canvasUrl.value += ""
                    }
                }
            }
        }
    }
}