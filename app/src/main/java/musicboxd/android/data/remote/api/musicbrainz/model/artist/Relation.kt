package musicboxd.android.data.remote.api.musicbrainz.model.artist

import kotlinx.serialization.Serializable

@Serializable
data class Relation(
    val type: String,
    val url: Url
)