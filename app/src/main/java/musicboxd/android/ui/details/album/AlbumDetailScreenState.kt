package musicboxd.android.ui.details.album

import kotlinx.coroutines.flow.Flow


data class AlbumDetailScreenState(
    val covertArtImgUrl: Flow<String>,
    val albumImgUrl: String,
    val albumTitle: String,
    val artists: List<String>,
    val wikipediaExtractText: Flow<String>,
    val releaseDate: String
)
