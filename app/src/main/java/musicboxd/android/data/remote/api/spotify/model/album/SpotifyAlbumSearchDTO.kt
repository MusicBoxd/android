package musicboxd.android.data.remote.api.spotify.model.album

import kotlinx.serialization.Serializable

@Serializable
data class SpotifyAlbumSearchDTO(
    val albums: Albums
)