package musicboxd.android.data.remote.api.spotify.model.tracklist

import kotlinx.serialization.Serializable

@Serializable

data class Artist(
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)