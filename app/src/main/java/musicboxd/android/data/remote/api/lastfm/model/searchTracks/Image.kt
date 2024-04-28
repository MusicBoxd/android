package musicboxd.android.data.remote.api.lastfm.model.searchTracks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Image(
    @SerialName("#text")
    val imgURl: String,
    val size: String
)