package musicboxd.android.data.remote.api.spotify

import musicboxd.android.data.remote.api.spotify.model.artists.SpotifyArtistSearchData

interface SpotifyAPIRepo {
    suspend fun searchArtists(
        artistName: String,
        limit: String,
        authorizationToken: String
    ): SpotifyArtistSearchData
}