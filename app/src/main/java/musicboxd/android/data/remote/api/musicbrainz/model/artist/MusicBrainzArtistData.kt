package musicboxd.android.data.remote.api.musicbrainz.model.artist

import kotlinx.serialization.Serializable

@Serializable
data class MusicBrainzArtistData(
    val relations: List<Relation>
)