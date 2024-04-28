package musicboxd.android.data.remote.api.spotify.model.token

import kotlinx.serialization.Serializable

@Serializable
data class SpotifyToken(
    val accessToken: String,
    val accessTokenExpirationTimestampMs: Long,
    val clientId: String,
    val isAnonymous: Boolean
)