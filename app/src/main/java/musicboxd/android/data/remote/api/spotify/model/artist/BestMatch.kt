package musicboxd.android.data.remote.api.spotify.model.artist

import kotlinx.serialization.Serializable

@Serializable
data class BestMatch(
    val items: List<ItemX>
)