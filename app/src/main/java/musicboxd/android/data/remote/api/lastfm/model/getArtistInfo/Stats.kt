package musicboxd.android.data.remote.api.lastfm.model.getArtistInfo

import kotlinx.serialization.Serializable

@Serializable

data class Stats(
    val listeners: String,
    val playcount: String
)