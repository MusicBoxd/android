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
import musicboxd.android.data.remote.api.APIResult
import musicboxd.android.data.remote.api.lastfm.LastFMAPIRepo
import musicboxd.android.data.remote.api.songlink.SongLinkRepo
import musicboxd.android.data.remote.api.spotify.SpotifyAPIRepo
import musicboxd.android.data.remote.api.spotify.charts.SpotifyChartsAPIRepo
import musicboxd.android.data.remote.api.spotify.model.album.Albums
import musicboxd.android.data.remote.api.spotify.model.artist_search.ExternalUrls
import musicboxd.android.data.remote.api.spotify.model.artist_search.Followers
import musicboxd.android.data.remote.api.spotify.model.artist_search.Item
import musicboxd.android.data.remote.api.spotify.model.topTracks.TopTracksDTO
import musicboxd.android.ui.details.album.AlbumDetailScreenState
import musicboxd.android.ui.details.model.ItemExternalLink
import musicboxd.android.ui.search.SearchScreenViewModel
import musicboxd.android.utils.customConfig
import org.jsoup.Jsoup
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val spotifyAPIRepo: SpotifyAPIRepo,
    private val songLinkRepo: SongLinkRepo,
    private val lastFMAPIRepo: LastFMAPIRepo,
    spotifyChartsAPIRepo: SpotifyChartsAPIRepo
) : SearchScreenViewModel(spotifyAPIRepo, spotifyChartsAPIRepo) {
    var albumScreenState = AlbumDetailScreenState(
        covertArtImgUrl = flow { },
        albumImgUrl = "",
        albumTitle = "",
        artists = listOf(),
        albumWiki = flow { },
        releaseDate = "",
        trackList = flow { }, itemType = ""
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
        canvasUrl.value = emptyList()
        albumExternalLinks.value = emptyList()
        spotifyToken?.accessToken?.let {
            viewModelScope.launch {
                awaitAll(async {
                    getAlbumInfoDataFromLastFM(albumName, artistName)
                }, async {
                    loadPalette(loadArtistImg, artistID, it)
                }, async {
                    loadTrackListOfAnAlbumData(it, albumID)
                }, async {
                    loadExternalLinks(false, "", albumID)
                })
            }.invokeOnCompletion {
                loadCanvases()
            }
        }
    }

    private suspend fun loadTopTracksOfAnArtist(artistID: String, accessToken: String) {
        when (val getTopTracksOfAnArtist = spotifyAPIRepo.getTopTracksOfAnArtist(
            artistID, accessToken
        )) {
            is APIResult.Failure -> TODO()
            is APIResult.Success -> {
                _topTracksDTO.emit(
                    getTopTracksOfAnArtist.data
                )
            }
        }
    }

    private suspend fun loadArtistBio(artistName: String) {
        when (val getArtistInfoData = lastFMAPIRepo.getArtistInfo(
            artistName,
            LAST_FM_API_KEY
        )) {
            is APIResult.Failure -> TODO()
            is APIResult.Success -> {
                val modifiedText =
                    getArtistInfoData.data.artist.bio.content.substringBefore("<a href=").trim()
                _artistWiki.emit(
                    modifiedText + if (modifiedText.endsWith(".")) "" else "."
                )
            }
        }
    }

    private suspend fun loadArtistImageFromLastFM(artistName: String) {
        val imageItem = withContext(Dispatchers.IO) {
            Jsoup.connect(
                "https://www.last.fm/music/${
                    artistName.replace(
                        " ",
                        "+"
                    )
                }/+images"
            ).customConfig().get()
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
            ).customConfig().get()
        }.toString().substringAfter("src=\"https://lastfm.freetls.fastly.net")
            .substringBefore("\">")
        _lastFMImage.emit(imgURL)
    }

    private suspend fun loadArtistSocialHandles(artistID: String) {
        val socialsItem = withContext(Dispatchers.IO) {
            Jsoup.connect("https://open.spotify.com/artist/$artistID").customConfig().get()
        }.toString().substringAfter("<div class=\"ZdmJvgeayszd6ZvSl5B6\">")
            .substringBefore("<div class=\"iQxdxLc2HsEnJMZt0Us4\">")
            .split("<li class=\"p44AskfOeVB1s75duZoC\"><a rel=\"noopener noreferrer\" target=\"_blank\" href=\"")
            .map {
                it.substringAfter("https://").substringBefore("\" class")
            }.filter { it.contains(".com") }.map { "https://".plus(it) }
        artistSocials.value = socialsItem
    }

    private suspend fun loadAlbumsOfAnArtist(artistID: String, accessToken: String) {
        when (val getAlbumsOfAnArtistData = spotifyAPIRepo.getAlbumsOfAnArtist(
            artistID, accessToken
        )) {
            is APIResult.Failure -> TODO()
            is APIResult.Success -> _artistAlbums.emit(
                getAlbumsOfAnArtistData.data
            )
        }
    }

    private suspend fun loadTrackListOfAnAlbumData(authorizationToken: String, albumID: String) {
        when (val getTrackListOfAnAlbumData = spotifyAPIRepo.getTrackListOfAnAlbum(
            authorizationToken = authorizationToken, albumID = albumID
        )) {
            is APIResult.Failure -> TODO()
            is APIResult.Success -> albumScreenState =
                albumScreenState.copy(trackList = flow {
                    emit(
                        getTrackListOfAnAlbumData.data.items
                    )
                })
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
                    loadTopTracksOfAnArtist(artistID, it.accessToken)
                }, async {
                    loadArtistBio(artistName)
                }, async {
                    loadAlbumsOfAnArtist(artistID, it.accessToken)
                }, async {
                    loadArtistSocialHandles(artistID)
                }, async {
                    loadArtistImageFromLastFM(artistName)
                })
            }
        }
    }

    fun loadArtistMetaData(artistID: String) {
        viewModelScope.launch {
            spotifyToken?.let {
                when (val artistData = spotifyAPIRepo.getArtistData(artistID, it.accessToken)) {
                    is APIResult.Failure -> TODO()
                    is APIResult.Success -> {
                        albumScreenState =
                            albumScreenState.copy(covertArtImgUrl = flowOf(artistData.data.images.first().url))
                        artistInfo.value = Item(
                            external_urls = artistData.data.external_urls,
                            followers = artistData.data.followers,
                            genres = artistData.data.genres,
                            href = "",
                            id = artistData.data.id,
                            images = artistData.data.images,
                            name = artistData.data.name,
                            popularity = artistData.data.popularity,
                            type = artistData.data.type,
                            uri = artistData.data.uri
                        )
                    }
                }
            }
        }
    }

    fun loadExternalLinks(isTrack: Boolean, trackID: String, albumID: String) {
        albumExternalLinks.value = emptyList()
        viewModelScope.launch {
            val itemType = if (isTrack) "track" else "album"
            val itemId = if (isTrack) trackID else albumID
            when (val albumExternalLinksAPIData =
                songLinkRepo.getLinks("https://open.spotify.com/$itemType/$itemId")) {
                is APIResult.Failure -> TODO()
                is APIResult.Success -> {
                    albumExternalLinks.value = listOf(
                        ItemExternalLink(
                            imgURL = "https://play-lh.googleusercontent.com/WKVNPckIxFKWO22Cu1o7zZf8l36-caVIkneZD7ocSBgGbSWYbnkyE4iECGI_qEhHkuQ",
                            externalLink = albumExternalLinksAPIData.data.linksByPlatform.amazonMusic.url
                        ),
                        ItemExternalLink(
                            imgURL = "https://asset.brandfetch.io/id4G4LfCWL/idAXHZ3WLJ.jpeg",
                            externalLink = albumExternalLinksAPIData.data.linksByPlatform.anghami.url
                        ),
                        ItemExternalLink(
                            imgURL = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5f/Apple_Music_icon.svg/2048px-Apple_Music_icon.svg.png",
                            externalLink = albumExternalLinksAPIData.data.linksByPlatform.appleMusic.url
                        ),
                        ItemExternalLink(
                            imgURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRoAajHlXLDtUtidNRDFDlCOidk_mXIbeLk4qtds_tuap4699CnsHylLd9rjEz9JKqtAPc&usqp=CAU",
                            externalLink = albumExternalLinksAPIData.data.linksByPlatform.audiomack.url
                        ),
                        ItemExternalLink(
                            imgURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQnPbBTXM_f1seJ-qjOxEkRLJQ1c6sbjkAyIQFKv2Am_Q&s",
                            externalLink = albumExternalLinksAPIData.data.linksByPlatform.boomplay.url
                        ),
                        ItemExternalLink(
                            imgURL = "https://store-images.s-microsoft.com/image/apps.46581.13510798886741797.6272719c-6e87-457e-89fa-c3c223a1c07a.f8b3708e-1ddf-4cb3-b201-9263bf813aae?h=210",
                            externalLink = albumExternalLinksAPIData.data.linksByPlatform.deezer.url
                        ),
                        ItemExternalLink(
                            imgURL = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/df/ITunes_logo.svg/438px-ITunes_logo.svg.png",
                            externalLink = albumExternalLinksAPIData.data.linksByPlatform.itunes.url
                        ),
                        ItemExternalLink(
                            imgURL = "https://images.squarespace-cdn.com/content/v1/55915377e4b05e44b9c13bca/1535515150211-O2O5767XRDJRF1WG2A1Q/Napster-Logo-.png?format=2500w",
                            externalLink = albumExternalLinksAPIData.data.linksByPlatform.napster.url
                        ),
                        ItemExternalLink(
                            imgURL = "https://store-images.s-microsoft.com/image/apps.31523.9007199266244713.666a3fe2-1a3e-4c9f-8301-d7fb23270871.7fd63e6c-7ca0-4e8a-9eed-ceee957307f2?h=210",
                            externalLink = albumExternalLinksAPIData.data.linksByPlatform.pandora.url
                        ),
                        ItemExternalLink(
                            imgURL = "https://store-images.s-microsoft.com/image/apps.22696.14398308773733109.53eed167-276f-443c-a7a5-c0d635242775.283cc376-189e-4777-8a82-29f500a9de4f?h=210",
                            externalLink = albumExternalLinksAPIData.data.linksByPlatform.soundcloud.url
                        ),
                        ItemExternalLink(
                            imgURL = "https://store-images.s-microsoft.com/image/apps.10546.13571498826857201.6603a5e2-631f-4f29-9b08-f96589723808.dc893fe0-ecbc-4846-9ac6-b13886604095?h=210",
                            externalLink = albumExternalLinksAPIData.data.linksByPlatform.spotify.url
                        ),
                        ItemExternalLink(
                            imgURL = "https://store-images.s-microsoft.com/image/apps.54003.13740903646068838.738514b4-7fb6-4210-af7f-9aa318dd0a71.d958b4a6-53ef-42fc-96c5-08796bbcb894?h=210",
                            externalLink = albumExternalLinksAPIData.data.linksByPlatform.tidal.url
                        ),
                        ItemExternalLink(
                            imgURL = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e7/Yandex_Music_icon.svg/165px-Yandex_Music_icon.svg.png",
                            externalLink = albumExternalLinksAPIData.data.linksByPlatform.yandex.url
                        ),
                        ItemExternalLink(
                            imgURL = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/09/YouTube_full-color_icon_%282017%29.svg/512px-YouTube_full-color_icon_%282017%29.svg.png?20240107144800",
                            externalLink = albumExternalLinksAPIData.data.linksByPlatform.youtube.url
                        ),
                        ItemExternalLink(
                            imgURL = "https://play-lh.googleusercontent.com/GnYnNfKBr2nysHBYgYRCQtcv_RRNN0Sosn47F5ArKJu89DMR3_jHRAazoIVsPUoaMg=w240-h480",
                            externalLink = albumExternalLinksAPIData.data.linksByPlatform.youtubeMusic.url
                        ),
                    )
                }
            }

        }
    }

    private suspend fun loadPalette(
        loadArtistImg: Boolean,
        artistID: String,
        spotifyToken: String
    ) {
        if (loadArtistImg) {
            when (val getArtistData = spotifyAPIRepo.getArtistData(artistID, spotifyToken)) {
                is APIResult.Failure -> {

                }

                is APIResult.Success -> {
                    albumScreenState = albumScreenState.copy(covertArtImgUrl = flow {
                        emit(getArtistData.data.images.first().url)
                    })
                }
            }
        }
        val inputStream = withContext(Dispatchers.IO) {
            URL(albumScreenState.albumImgUrl).openConnection().getInputStream()
        }
        val albumCoverArtBitmap = BitmapFactory.decodeStream(inputStream)
        val paletteForAlbumCoverArt = Palette.from(albumCoverArtBitmap).generate()
        paletteColors.value = paletteForAlbumCoverArt
        previewCardColor.value =
            Color(paletteForAlbumCoverArt.getDarkMutedColor(Color.Black.toArgb()))
    }

    private suspend fun getAlbumInfoDataFromLastFM(
        albumName: String,
        artistName: String
    ) {
        when (val getAlbumInfoData = lastFMAPIRepo.getAlbumInfo(
            artistName,
            albumName,
            LAST_FM_API_KEY
        )) {
            is APIResult.Failure -> {
                albumScreenState = albumScreenState.copy(albumWiki = flow {
                    emit(
                        getAlbumInfoData.message
                    )
                })
            }

            is APIResult.Success -> {
                albumScreenState = albumScreenState.copy(albumWiki = flow {
                    emit(
                        getAlbumInfoData.data.album.wiki.content.substringBefore("<a href=").trim()
                    )
                })
            }
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