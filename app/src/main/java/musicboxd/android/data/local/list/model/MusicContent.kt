package musicboxd.android.data.local.list.model

import kotlinx.serialization.Serializable

@Serializable
data class MusicContent(
    val itemName: String,
    val itemImgUrl: String,
    val itemSpotifyUri: String
)
