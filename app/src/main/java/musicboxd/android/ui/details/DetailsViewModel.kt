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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import musicboxd.android.data.remote.api.spotify.SpotifyAPIRepo
import musicboxd.android.data.remote.api.wikipedia.WikipediaAPIRepo
import musicboxd.android.ui.details.album.AlbumDetailScreenState
import musicboxd.android.ui.search.SearchScreenViewModel
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val spotifyAPIRepo: SpotifyAPIRepo, private val wikipediaAPIRepo: WikipediaAPIRepo
) : SearchScreenViewModel(spotifyAPIRepo) {
    var albumScreenState = AlbumDetailScreenState(
        covertArtImgUrl = flow { },
        albumImgUrl = "",
        albumTitle = "",
        artists = listOf(),
        wikipediaExtractText = flow { },
        releaseDate = "",
        trackList = flow { }
    )
    var previewCardColor = mutableStateOf(Color.Black)
    var paletteColors = mutableStateOf<Palette?>(null)
    fun loadAlbumInfo(artistID: String, albumName: String, albumID: String) {
        spotifyToken?.accessToken?.let {
            viewModelScope.launch {
                awaitAll(
                    async {
                        albumScreenState = albumScreenState.copy(wikipediaExtractText = flow {
                            emit(wikipediaAPIRepo.getSummary(albumName).extract)
                        })
                    },
                    async {
                        albumScreenState = albumScreenState.copy(covertArtImgUrl = flow {
                            emit(spotifyAPIRepo.getArtistData(artistID, it).images.first().url)
                        })
                        val inputStream = withContext(Dispatchers.IO) {
                            URL(albumScreenState.albumImgUrl).openConnection()
                                .getInputStream()
                        }
                        val albumCoverArtBitmap = BitmapFactory.decodeStream(inputStream)
                        val paletteForAlbumCoverArt = Palette.from(albumCoverArtBitmap).generate()
                        paletteColors.value = paletteForAlbumCoverArt
                        previewCardColor.value =
                            Color(paletteForAlbumCoverArt.getDarkMutedColor(Color.Black.toArgb()))
                    },
                    async {
                        albumScreenState = albumScreenState.copy(trackList = flow {
                            emit(
                                spotifyAPIRepo.getTrackListOfAnAlbum(
                                    authorizationToken = it,
                                    albumID = albumID
                                ).items
                            )
                        })
                    }
                )
            }
        }
    }
}