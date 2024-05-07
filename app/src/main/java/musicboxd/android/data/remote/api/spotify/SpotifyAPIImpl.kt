package musicboxd.android.data.remote.api.spotify

import musicboxd.android.data.remote.api.APIResult
import musicboxd.android.data.remote.api.spotify.model.album.Albums
import musicboxd.android.data.remote.api.spotify.model.album.SpotifyAlbumSearchDTO
import musicboxd.android.data.remote.api.spotify.model.artist_search.SpotifyArtistSearchDTO
import musicboxd.android.data.remote.api.spotify.model.specific_artist.SpecificArtistFromSpotifyDTO
import musicboxd.android.data.remote.api.spotify.model.topTracks.TopTracksDTO
import musicboxd.android.data.remote.api.spotify.model.track.SpotifyTrackSearchDTO
import musicboxd.android.data.remote.api.spotify.model.tracklist.SpotifyAlbumTrackListDTO
import javax.inject.Inject

class SpotifyAPIImpl @Inject constructor(private val spotifyAPIService: SpotifyAPIService) :
    SpotifyAPIRepo {
    override suspend fun searchArtists(
        artistName: String,
        limit: String,
        authorizationToken: String
    ): APIResult<SpotifyArtistSearchDTO> {
        return try {
            APIResult.Success(
                spotifyAPIService.searchArtists(
                    artistName,
                    limit,
                    "Bearer ".plus(authorizationToken)
                )
            )
        } catch (_: Exception) {
            APIResult.Failure("Cannot fetch searchArtists in SpotifyImpl")
        }
    }

    override suspend fun getArtistData(
        id: String,
        authorizationToken: String
    ): APIResult<SpecificArtistFromSpotifyDTO> {
        return try {
            APIResult.Success(
                spotifyAPIService.getArtistData(
                    id,
                    "Bearer ".plus(authorizationToken)
                )
            )
        } catch (_: Exception) {
            APIResult.Failure("Cannot fetch getArtistData in SpotifyImpl")
        }
    }

    override suspend fun searchAlbums(
        albumName: String,
        limit: String,
        authorizationToken: String
    ): APIResult<SpotifyAlbumSearchDTO> {
        return try {
            APIResult.Success(
                spotifyAPIService.searchAlbums(
                    albumName,
                    limit,
                    "Bearer ".plus(authorizationToken)
                )
            )
        } catch (_: Exception) {
            APIResult.Failure("Cannot fetch searchAlbums in SpotifyImpl")
        }
    }

    override suspend fun getTrackListOfAnAlbum(
        albumID: String,
        authorizationToken: String
    ): APIResult<SpotifyAlbumTrackListDTO> {
        return try {
            APIResult.Success(
                spotifyAPIService.getTrackListOfAnAlbum(albumID, "Bearer ".plus(authorizationToken))
            )
        } catch (_: Exception) {
            APIResult.Failure("Cannot fetch getTrackListOfAnAlbum in SpotifyImpl")
        }
    }

    override suspend fun searchTracks(
        trackName: String,
        limit: String,
        authorizationToken: String
    ): APIResult<SpotifyTrackSearchDTO> {
        return try {
            APIResult.Success(
                spotifyAPIService.searchTracks(
                    trackName,
                    limit,
                    "Bearer ".plus(authorizationToken)
                )
            )
        } catch (_: Exception) {
            APIResult.Failure("Cannot fetch searchTracks in SpotifyImpl")
        }
    }

    override suspend fun getTopTracksOfAnArtist(
        artistId: String,
        authorizationToken: String
    ): APIResult<TopTracksDTO> {
        return try {
            APIResult.Success(
                spotifyAPIService.getTopTracksOfAnArtist(
                    artistId,
                    "Bearer ".plus(authorizationToken)
                )
            )
        } catch (_: Exception) {
            APIResult.Failure("Cannot fetch getTopTracksOfAnArtist in SpotifyImpl")
        }
    }

    override suspend fun getAlbumsOfAnArtist(
        artistId: String,
        authorizationToken: String
    ): APIResult<Albums> {
        return try {
            APIResult.Success(
                spotifyAPIService.getAlbumsOfAnArtist(artistId, "Bearer ".plus(authorizationToken))
            )
        } catch (_: Exception) {
            APIResult.Failure("Cannot fetch getAlbumsOfAnArtist in SpotifyImpl")
        }
    }

}