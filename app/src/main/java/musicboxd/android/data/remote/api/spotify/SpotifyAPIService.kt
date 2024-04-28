package musicboxd.android.data.remote.api.spotify

import musicboxd.android.data.remote.api.spotify.model.artists.SpotifyArtistSearchData
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyAPIService {

    @GET("search?type=artist&decorate_restrictions=false&best_match=true&include_external=audio")
    suspend fun searchArtists(
        @Query("q") artistName: String,
        @Query("limit") limit: String,
        @Header("Authorization") authorizationToken: String
    ): SpotifyArtistSearchData
}