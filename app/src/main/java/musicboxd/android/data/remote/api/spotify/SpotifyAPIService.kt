package musicboxd.android.data.remote.api.spotify

import androidx.annotation.Keep
import musicboxd.android.data.remote.api.spotify.model.album.SpotifyAlbumSearchDTO
import musicboxd.android.data.remote.api.spotify.model.artist.SpotifyArtistSearchDTO
import musicboxd.android.data.remote.api.spotify.model.track.SpotifyTrackSearchDTO
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

@Keep
interface SpotifyAPIService {

    @GET("search?type=artist&decorate_restrictions=false&best_match=true&include_external=audio")
    suspend fun searchArtists(
        @Query("q") artistName: String,
        @Query("limit") limit: String,
        @Header("Authorization") authorizationToken: String
    ): SpotifyArtistSearchDTO

    @GET("search?type=album")
    suspend fun searchAlbums(
        @Query("q") albumName: String,
        @Query("limit") limit: String,
        @Header("Authorization") authorizationToken: String
    ): SpotifyAlbumSearchDTO

    @GET("search?type=track")
    suspend fun searchTracks(
        @Query("q") trackName: String,
        @Query("limit") limit: String,
        @Header("Authorization") authorizationToken: String
    ): SpotifyTrackSearchDTO

}