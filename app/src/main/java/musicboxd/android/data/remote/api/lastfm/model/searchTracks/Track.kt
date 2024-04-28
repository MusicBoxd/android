package musicboxd.android.data.remote.api.lastfm.model.searchTracks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Track(
    val artist: String,
    @SerialName("image")
    val images: List<Image>,
    @SerialName("listeners")
    val listenersCount: String,
    val name: String,
    val url: String
)