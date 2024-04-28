package musicboxd.android.data.remote.api.lastfm.model.searchTracks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Results(
    @SerialName("opensearch:totalResults")
    val totalResults: String,
    @SerialName("trackmatches")
    val trackMatches: TrackMatches
)