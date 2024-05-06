package musicboxd.android.data.remote.api.lastfm.model.getAlbumInfo

import kotlinx.serialization.Serializable

@Serializable
data class Wiki(
    val content: String,
    val published: String,
    val summary: String
)