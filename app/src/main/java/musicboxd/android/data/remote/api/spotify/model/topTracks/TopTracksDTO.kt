package musicboxd.android.data.remote.api.spotify.model.topTracks

import kotlinx.serialization.Serializable

@Serializable
data class TopTracksDTO(
    val tracks: List<Track>
)