package musicboxd.android.data.remote.api.spotify.model.album

import kotlinx.serialization.Serializable

@Serializable

data class Albums(
    val items: List<Item>,
    val limit: Int,
    val offset: Int,
    val total: Int
)