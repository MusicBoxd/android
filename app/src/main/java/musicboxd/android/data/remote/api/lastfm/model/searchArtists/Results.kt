package musicboxd.android.data.remote.api.lastfm.model.searchArtists

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Results(
    @SerialName("artistmatches")
    val artistMatches: ArtistMatches,
    @SerialName("opensearch:totalResults")
    val totalResults: String
)