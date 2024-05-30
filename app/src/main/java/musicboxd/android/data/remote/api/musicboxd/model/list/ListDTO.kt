package musicboxd.android.data.remote.api.musicboxd.model.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListDTO(
    @SerialName("name")
    val listName: String,
    @SerialName("description")
    val lisDescription: String,
    @SerialName("uris")
    val spotifyURIs: List<String>,
    @SerialName("publicAccess")
    val isListPublic: Boolean
)
