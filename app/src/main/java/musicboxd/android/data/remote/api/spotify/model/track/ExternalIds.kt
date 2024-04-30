package musicboxd.android.data.remote.api.spotify.model.track

import kotlinx.serialization.Serializable

@Serializable

data class ExternalIds(
    val isrc: String
)