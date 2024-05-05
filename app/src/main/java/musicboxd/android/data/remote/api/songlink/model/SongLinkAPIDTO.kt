package musicboxd.android.data.remote.api.songlink.model

import kotlinx.serialization.Serializable

@Serializable
data class SongLinkAPIDTO(
    val linksByPlatform: LinksByPlatform
)