package musicboxd.android.data.remote.api.lastfm.model.getAlbumInfo

import kotlinx.serialization.Serializable

@Serializable
data class GetAlbumInfoFromLastFMDTO(
    val album: Album
)