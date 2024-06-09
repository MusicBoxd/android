package musicboxd.android.data.remote.api.musicboxd.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val headerPic: String = "",
    @SerialName("id")
    val remoteId: Long,
    val name: String,
    val password: String,
    val profilePic: String = "",
    val userRole: String,
    val username: String
)