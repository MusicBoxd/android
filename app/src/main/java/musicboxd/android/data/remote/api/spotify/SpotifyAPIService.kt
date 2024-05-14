package musicboxd.android.data.remote.api.spotify

import androidx.annotation.Keep
import musicboxd.android.data.remote.api.spotify.model.album.Albums
import musicboxd.android.data.remote.api.spotify.model.album.SpotifyAlbumSearchDTO
import musicboxd.android.data.remote.api.spotify.model.artist_search.SpotifyArtistSearchDTO
import musicboxd.android.data.remote.api.spotify.model.specific_artist.SpecificArtistFromSpotifyDTO
import musicboxd.android.data.remote.api.spotify.model.topTracks.TopTracksDTO
import musicboxd.android.data.remote.api.spotify.model.track.SpotifyTrackSearchDTO
import musicboxd.android.data.remote.api.spotify.model.tracklist.SpotifyAlbumTrackListDTO
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

@Keep
interface SpotifyAPIService {

    @GET("search?type=artist&decorate_restrictions=false&best_match=true&include_external=audio")
    suspend fun searchArtists(
        @Query("q") artistName: String,
        @Query("limit") limit: String,
        @Header("Authorization") authorizationToken: String
    ): SpotifyArtistSearchDTO

    @GET("artists/{id}")
    suspend fun getArtistData(
        @Path("id") artistId: String,
        @Header("Authorization") authorizationToken: String
    ): SpecificArtistFromSpotifyDTO

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

    @GET("albums/{id}/tracks")
    suspend fun getTrackListOfAnAlbum(
        @Path("id") albumID: String,
        @Header("Authorization") authorizationToken: String
    ): SpotifyAlbumTrackListDTO

    @GET("artists/{id}/top-tracks")
    suspend fun getTopTracksOfAnArtist(
        @Path("id") artistId: String,
        @Header("Authorization") authorizationToken: String
    ): TopTracksDTO

    @GET("artists/{id}/albums")
    suspend fun getAlbumsOfAnArtist(
        @Path("id") artistId: String,
        @Header("Authorization") authorizationToken: String
    ): Albums

    @GET("tracks/{id}")
    suspend fun getATrack(
        @Path("id") trackID: String,
        @Header("Authorization") authorizationToken: String
    ): musicboxd.android.data.remote.api.spotify.model.tracklist.Item
}