package musicboxd.android.data.remote.api.musicbrainz.model.artist

import kotlinx.serialization.Serializable

@Serializable
data class Url(
    val id: String,
    val resource: String
)