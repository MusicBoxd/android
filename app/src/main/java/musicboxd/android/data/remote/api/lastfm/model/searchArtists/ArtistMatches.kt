package musicboxd.android.data.remote.api.lastfm.model.searchArtists

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistMatches(
    @SerialName("artist")
    val artists: List<Artist>
)