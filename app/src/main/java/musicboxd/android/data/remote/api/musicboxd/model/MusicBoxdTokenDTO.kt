package musicboxd.android.data.remote.api.musicboxd.model

import kotlinx.serialization.Serializable

@Serializable
data class MusicBoxdTokenDTO(
    val jwt: String,
    val refreshJwt: String,
    val userId: Int,
    val userRole: String,
    val username: String
)