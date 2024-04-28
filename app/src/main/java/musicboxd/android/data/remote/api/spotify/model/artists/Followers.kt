package musicboxd.android.data.remote.api.spotify.model.artists

import kotlinx.serialization.Serializable

@Serializable
data class Followers(
    val total: Int
)