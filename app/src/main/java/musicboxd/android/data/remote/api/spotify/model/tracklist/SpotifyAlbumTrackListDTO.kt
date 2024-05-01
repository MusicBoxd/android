package musicboxd.android.data.remote.api.spotify.model.tracklist

import kotlinx.serialization.Serializable

@Serializable
data class SpotifyAlbumTrackListDTO(
    val items: List<Item>,
    val total: Int
)