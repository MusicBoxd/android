package musicboxd.android.data.remote.api.spotify.model.specific_artist

import kotlinx.serialization.Serializable
import musicboxd.android.data.remote.api.spotify.model.artist_search.ExternalUrls
import musicboxd.android.data.remote.api.spotify.model.artist_search.Followers
import musicboxd.android.data.remote.api.spotify.model.artist_search.Image

@Serializable
data class SpecificArtistFromSpotifyDTO(
    val external_urls: ExternalUrls,
    val followers: Followers,
    val genres: List<String>,
    val id: String,
    val images: List<Image>,
    val name: String,
    val popularity: Int,
    val type: String,
    val uri: String
)