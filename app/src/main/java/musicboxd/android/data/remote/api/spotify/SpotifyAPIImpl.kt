package musicboxd.android.data.remote.api.spotify

import musicboxd.android.data.remote.api.spotify.model.album.SpotifyAlbumSearchDTO
import musicboxd.android.data.remote.api.spotify.model.artist.SpotifyArtistSearchDTO
import musicboxd.android.data.remote.api.spotify.model.track.SpotifyTrackSearchDTO
import javax.inject.Inject

class SpotifyAPIImpl @Inject constructor(private val spotifyAPIService: SpotifyAPIService) :
    SpotifyAPIRepo {
    override suspend fun searchArtists(
        artistName: String,
        limit: String,
        authorizationToken: String
    ): SpotifyArtistSearchDTO {
        return spotifyAPIService.searchArtists(
            artistName,
            limit,
            "Bearer ".plus(authorizationToken)
        )
    }

    override suspend fun searchAlbums(
        albumName: String,
        limit: String,
        authorizationToken: String
    ): SpotifyAlbumSearchDTO {
        return spotifyAPIService.searchAlbums(
            albumName,
            limit,
            "Bearer ".plus(authorizationToken)
        )
    }

    override suspend fun searchTracks(
        trackName: String,
        limit: String,
        authorizationToken: String
    ): SpotifyTrackSearchDTO {
        return spotifyAPIService.searchTracks(
            trackName,
            limit,
            "Bearer ".plus(authorizationToken)
        )
    }

}