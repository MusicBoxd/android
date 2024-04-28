package musicboxd.android.data.remote.api.spotify.model.artists

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpotifyArtistSearchData(
    @SerialName("best_match")
    val bestMatch: BestMatch,
    val artists: Artists
)