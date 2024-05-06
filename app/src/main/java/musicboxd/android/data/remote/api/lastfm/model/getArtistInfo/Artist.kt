package musicboxd.android.data.remote.api.lastfm.model.getArtistInfo

import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    val bio: Bio,
    val name: String,
    val stats: Stats,
    val url: String
)