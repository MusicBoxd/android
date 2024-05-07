package musicboxd.android.ui.details.album

import kotlinx.coroutines.flow.Flow
import musicboxd.android.data.remote.api.spotify.model.tracklist.Item


data class AlbumDetailScreenState(
    val artistId: String,
    val covertArtImgUrl: Flow<String>,
    val albumImgUrl: String,
    val albumTitle: String,
    val artists: List<String>,
    val albumWiki: Flow<String>,
    val releaseDate: String,
    val trackList: Flow<List<Item>>,
    val itemType: String
)
