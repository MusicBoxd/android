package musicboxd.android.data.remote.api.lastfm.model.getArtistInfo

import kotlinx.serialization.Serializable

@Serializable

data class Bio(
    val content: String,
    val links: Links,
    val published: String,
    val summary: String
)