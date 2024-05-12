package musicboxd.android.data.remote.api.spotify.charts.model

import kotlinx.serialization.Serializable

@Serializable

data class AlbumMetadata(
    val albumName: String,
    val albumUri: String,
    val artists: List<Artist>,
    val displayImageUri: String,
    val releaseDate: String
)