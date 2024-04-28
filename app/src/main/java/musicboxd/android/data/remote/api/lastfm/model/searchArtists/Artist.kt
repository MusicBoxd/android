package musicboxd.android.data.remote.api.lastfm.model.searchArtists

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    @SerialName("listeners")
    val listenersCount: String,
    val name: String,
    val mbid: String,
    @SerialName("url")
    val lastFMURL: String
)