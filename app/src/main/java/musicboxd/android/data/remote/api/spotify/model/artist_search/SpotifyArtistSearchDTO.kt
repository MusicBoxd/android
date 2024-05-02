package musicboxd.android.data.remote.api.spotify.model.artist_search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpotifyArtistSearchDTO(
    @SerialName("best_match")
    val bestMatch: BestMatch,
    val artists: Artists
)