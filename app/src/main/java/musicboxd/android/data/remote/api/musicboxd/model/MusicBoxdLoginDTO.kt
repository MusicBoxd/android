package musicboxd.android.data.remote.api.musicboxd.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MusicBoxdLoginDTO(
    @SerialName("username")
    val userName: String,
    val password: String
)
