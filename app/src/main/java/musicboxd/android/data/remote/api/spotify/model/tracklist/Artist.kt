package musicboxd.android.data.remote.api.spotify.model.tracklist

import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    val id: String,
    val name: String,
    val uri: String
)