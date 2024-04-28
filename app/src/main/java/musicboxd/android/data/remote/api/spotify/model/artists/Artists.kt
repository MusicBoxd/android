package musicboxd.android.data.remote.api.spotify.model.artists

import kotlinx.serialization.Serializable

@Serializable
data class Artists(
    val href: String,
    val items: List<Item>,
    val limit: Int,
    val offset: Int,
    val total: Int
)