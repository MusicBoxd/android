package musicboxd.android.data.remote.api.spotify.model.specific_artist

import kotlinx.serialization.Serializable

@Serializable
data class Followers(
    val total: Int
)