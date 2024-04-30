package musicboxd.android.data.remote.api.musicbrainz

import androidx.annotation.Keep
import musicboxd.android.data.remote.api.musicbrainz.model.artist.MusicBrainzArtistData
import retrofit2.http.GET
import retrofit2.http.Path

@Keep
interface MusicBrainzAPIService {
    @GET("artist/{mbid}?inc=url-rels&fmt=json")
    suspend fun getArtistMetaData(@Path("mbid") artistMBID: String): MusicBrainzArtistData
}