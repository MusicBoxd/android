package musicboxd.android.data.remote.api.musicboxd.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpDTO(
    @SerialName("name")
    val userProfileName: String,
    val username: String,
    val password: String
)
