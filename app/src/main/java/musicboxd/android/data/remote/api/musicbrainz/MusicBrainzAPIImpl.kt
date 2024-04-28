package musicboxd.android.data.remote.api.musicbrainz

import musicboxd.android.data.remote.api.musicbrainz.model.artist.MusicBrainzArtistData
import javax.inject.Inject

class MusicBrainzAPIImpl @Inject constructor(private val musicBrainzAPIService: MusicBrainzAPIService) :
    MusicBrainzAPIRepo {
    override suspend fun getArtistMetaData(artistMBID: String): MusicBrainzArtistData {
        return musicBrainzAPIService.getArtistMetaData(artistMBID)
    }
}