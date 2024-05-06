package musicboxd.android.data.remote.api.lastfm.model.getArtistInfo

import kotlinx.serialization.Serializable

@Serializable
data class GetArtistInfoFromLastFM(
    val artist: Artist
)