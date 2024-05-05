package musicboxd.android.data.remote.api.songlink

import musicboxd.android.data.remote.api.songlink.model.LinksByPlatform
import musicboxd.android.data.remote.api.songlink.model.SongLinkAPIDTO
import javax.inject.Inject

class SongLinkImpl @Inject constructor(private val songLinkAPIService: SongLinkAPIService) :
    SongLinkRepo {
    override suspend fun getLinks(spotifyUrl: String): SongLinkAPIDTO {
        return try {
            songLinkAPIService.getLinks(spotifyUrl)
        } catch (_: Exception) {
            SongLinkAPIDTO(LinksByPlatform())
        }
    }
}