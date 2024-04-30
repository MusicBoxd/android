package musicboxd.android.data.remote.api.spotify.model.track

import kotlinx.serialization.Serializable

@Serializable

data class Tracks(
    val href: String,
    val items: List<Item>,
    val limit: Int,
    val next: String,
    val offset: Int,
    val total: Int
)