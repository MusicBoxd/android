package musicboxd.android.data.remote.api.songlink

import musicboxd.android.data.remote.api.APIResult
import musicboxd.android.data.remote.api.songlink.model.SongLinkAPIDTO
import javax.inject.Inject

class SongLinkImpl @Inject constructor(private val songLinkAPIService: SongLinkAPIService) :
    SongLinkRepo {
    override suspend fun getLinks(spotifyUrl: String): APIResult<SongLinkAPIDTO> {
        return try {
            APIResult.Success(songLinkAPIService.getLinks(spotifyUrl))
        } catch (_: Exception) {
            APIResult.Failure("Cannot fetch getLinks")
        }
    }
}