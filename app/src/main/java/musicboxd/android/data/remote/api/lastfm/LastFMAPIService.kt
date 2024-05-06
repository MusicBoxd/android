package musicboxd.android.data.remote.api.lastfm

import androidx.annotation.Keep
import musicboxd.android.data.remote.api.lastfm.model.getAlbumInfo.GetAlbumInfoFromLastFMDTO
import musicboxd.android.data.remote.api.lastfm.model.getArtistInfo.GetArtistInfoFromLastFM
import musicboxd.android.data.remote.api.lastfm.model.searchAlbums.SearchAlbumsFromLastFm
import musicboxd.android.data.remote.api.lastfm.model.searchArtists.SearchArtists
import musicboxd.android.data.remote.api.lastfm.model.searchTracks.SearchTracks
import retrofit2.http.GET
import retrofit2.http.Query

@Keep
interface LastFMAPIService {

    @GET("2.0/?method=artist.search&format=json")
    suspend fun searchArtists(
        @Query("artist") artistName: String,
        @Query("api_key") apiKey: String
    ): SearchArtists

    @GET("2.0/?method=track.search&format=json")
    suspend fun searchTracks(
        @Query("track") trackName: String,
        @Query("api_key") apiKey: String
    ): SearchTracks

    @GET("2.0/?method=album.search&format=json")
    suspend fun searchAlbums(
        @Query("album") albumName: String,
        @Query("api_key") apiKey: String
    ): SearchAlbumsFromLastFm

    @GET("2.0/?method=artist.getinfo&format=json")
    suspend fun getArtistInfo(
        @Query("artist") artistName: String,
        @Query("api_key") apiKey: String
    ): GetArtistInfoFromLastFM

    @GET("2.0/?method=album.getinfo&format=json")
    suspend fun getAlbumInfo(
        @Query("artist") artistName: String,
        @Query("album") albumName: String,
        @Query("api_key") apiKey: String
    ): GetAlbumInfoFromLastFMDTO
}