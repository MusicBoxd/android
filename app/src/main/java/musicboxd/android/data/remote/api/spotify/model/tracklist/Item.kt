package musicboxd.android.data.remote.api.spotify.model.tracklist

import kotlinx.serialization.Serializable

@Serializable

data class Item(
    val artists: List<Artist>,
    val explicit: Boolean,
    val id: String,
    val name: String,
    val preview_url: String = "",
    val track_number: Int,
    val type: String,
    val uri: String
)