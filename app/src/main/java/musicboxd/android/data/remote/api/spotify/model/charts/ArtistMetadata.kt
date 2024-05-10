package musicboxd.android.data.remote.api.spotify.model.charts

import kotlinx.serialization.Serializable

@Serializable

data class ArtistMetadata(
    val artistName: String,
    val artistUri: String,
    val displayImageUri: String,
)