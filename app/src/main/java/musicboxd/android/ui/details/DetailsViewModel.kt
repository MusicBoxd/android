package musicboxd.android.ui.details

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import musicboxd.android.data.remote.api.spotify.SpotifyAPIRepo
import musicboxd.android.data.remote.api.wikipedia.WikipediaAPIRepo
import musicboxd.android.ui.details.album.AlbumDetailScreenState
import musicboxd.android.ui.search.SearchScreenViewModel
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