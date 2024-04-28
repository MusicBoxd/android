package musicboxd.android.data.remote.api.spotify

import musicboxd.android.data.remote.api.spotify.model.artists.SpotifyArtistSearchData
import javax.inject.Inject

class SpotifyAPIImpl @Inject constructor(private val spotifyAPIService: SpotifyAPIService) :
    SpotifyAPIRepo {
    override suspend fun searchArtists(
        artistName: String,
        limit: String,
        authorizationToken: String
    ): SpotifyArtistSearchData {
        return spotifyAPIService.searchArtists(
            artistName,
            limit,
            "Bearer ".plus(authorizationToken)
        )
    }

}