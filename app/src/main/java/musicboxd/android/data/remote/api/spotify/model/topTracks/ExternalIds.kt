package musicboxd.android.data.remote.api.spotify.model.topTracks

import kotlinx.serialization.Serializable

@Serializable

data class ExternalIds(
    val isrc: String
)