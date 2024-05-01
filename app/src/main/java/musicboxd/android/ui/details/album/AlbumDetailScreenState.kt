package musicboxd.android.ui.details.album

import kotlinx.coroutines.flow.Flow
import musicboxd.android.data.remote.api.spotify.model.tracklist.Item


data class AlbumDetailScreenState(
    val covertArtImgUrl: Flow<String>,
    val albumImgUrl: String,
    val albumTitle: String,
    val artists: List<String>,
    val wikipediaExtractText: Flow<String>,
    val releaseDate: String,
    val trackList: Flow<List<Item>>
)
