package musicboxd.android.data.remote.api.spotify.charts.model

import kotlinx.serialization.Serializable

@Serializable

data class Artist(
    val externalUrl: String,
    val name: String,
    val spotifyUri: String
)