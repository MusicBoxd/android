package musicboxd.android.data.remote.api.spotify.model.topTracks

import kotlinx.serialization.Serializable

@Serializable

data class ArtistX(
    val external_urls: ExternalUrlsXXX,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)