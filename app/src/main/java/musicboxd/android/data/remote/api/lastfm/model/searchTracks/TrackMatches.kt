package musicboxd.android.data.remote.api.lastfm.model.searchTracks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackMatches(
    @SerialName("track")
    val tracks: List<Track>
)