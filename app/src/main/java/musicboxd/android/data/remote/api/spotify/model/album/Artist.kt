package musicboxd.android.data.remote.api.spotify.model.album

import kotlinx.serialization.Serializable

@Serializable

data class Artist(
    val external_urls: ExternalUrlsX,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)