package musicboxd.android.data.remote.api.spotify.model.track

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val album: Album,
    val artists: List<ArtistX>,
    val available_markets: List<String>,
    val disc_number: Int,
    val duration_ms: Int,
    val explicit: Boolean,
    val external_ids: ExternalIds,
    val external_urls: ExternalUrlsXXX,
    val href: String,
    val id: String,
    val is_local: Boolean,
    val name: String,
    val popularity: Int,
    val preview_url: String,
    val track_number: Int,
    val type: String,
    val uri: String
)