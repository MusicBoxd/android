package musicboxd.android.data.remote.api.lastfm.model.searchAlbums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumMatches(
    @SerialName("album")
    val albums: List<Album>
)