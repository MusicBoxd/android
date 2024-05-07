package musicboxd.android.data.remote.api.musicbrainz

import musicboxd.android.data.remote.api.APIResult
import musicboxd.android.data.remote.api.musicbrainz.model.artist.MusicBrainzArtistData
import javax.inject.Inject

class MusicBrainzAPIImpl @Inject constructor(private val musicBrainzAPIService: MusicBrainzAPIService) :
    MusicBrainzAPIRepo {
    override suspend fun getArtistMetaData(artistMBID: String): APIResult<MusicBrainzArtistData> {
        return try {
            APIResult.Success(musicBrainzAPIService.getArtistMetaData(artistMBID))
        } catch (_: Exception) {
            APIResult.Failure("Cannot Fetch getArtistMetaData")
        }
    }
}