package musicboxd.android.data.remote.api.lastfm.model.searchAlbums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Album(
    @SerialName("artist")
    val artistName: String,
    @SerialName("image")
    val albumArts: List<Image>,
    @SerialName("name")
    val albumName: String,
    @SerialName("url")
    val lastFMURL: String
)