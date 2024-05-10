package musicboxd.android.data.remote.api.spotify.model.charts

import kotlinx.serialization.Serializable

@Serializable

data class TrackMetadata(
    val artists: List<Artist>,
    val displayImageUri: String,
    val releaseDate: String,
    val trackName: String,
    val trackUri: String
)