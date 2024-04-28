package musicboxd.android.data.remote.api.lastfm.model.searchAlbums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Results(
    @SerialName("albummatches")
    val albumMatches: AlbumMatches,
    @SerialName("opensearch:totalResults")
    val totalResults: String
)