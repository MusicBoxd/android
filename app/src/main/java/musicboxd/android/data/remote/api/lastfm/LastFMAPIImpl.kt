package musicboxd.android.data.remote.api.lastfm

import musicboxd.android.data.remote.api.APIResult
import musicboxd.android.data.remote.api.lastfm.model.getAlbumInfo.GetAlbumInfoFromLastFMDTO
import musicboxd.android.data.remote.api.lastfm.model.getArtistInfo.GetArtistInfoFromLastFM
import musicboxd.android.data.remote.api.lastfm.model.searchAlbums.SearchAlbumsFromLastFm
import musicboxd.android.data.remote.api.lastfm.model.searchArtists.SearchArtists
import musicboxd.android.data.remote.api.lastfm.model.searchTracks.SearchTracks
import javax.inject.Inject

class LastFMAPIImpl @Inject constructor(private val lastFMAPIService: LastFMAPIService) :
    LastFMAPIRepo {
    override suspend fun getArtistInfo(
        artistName: String,
        apiKey: String
    ): APIResult<GetArtistInfoFromLastFM> {
        return try {
            APIResult.Success(lastFMAPIService.getArtistInfo(artistName, apiKey))
        } catch (_: Exception) {
            APIResult.Failure("Cannot fetch getArtistInfo")
        }
    }

    override suspend fun searchArtists(
        artistName: String,
        apiKey: String
    ): APIResult<SearchArtists> {
        return try {
            APIResult.Success(lastFMAPIService.searchArtists(artistName, apiKey))
        } catch (_: Exception) {
            APIResult.Failure("Cannot fetch searchArtists")
        }
    }

    override suspend fun searchTracks(trackName: String, apiKey: String): APIResult<SearchTracks> {
        return try {
            APIResult.Success(lastFMAPIService.searchTracks(trackName, apiKey))
        } catch (_: Exception) {
            APIResult.Failure("Cannot fetch searchTracks")
        }
    }

    override suspend fun searchAlbums(
        albumName: String,
        apiKey: String
    ): APIResult<SearchAlbumsFromLastFm> {
        return try {
            APIResult.Success(lastFMAPIService.searchAlbums(albumName, apiKey))
        } catch (_: Exception) {
            APIResult.Failure("Cannot fetch searchAlbums")
        }
    }

    override suspend fun getAlbumInfo(
        artistName: String,
        albumName: String,
        apiKey: String
    ): APIResult<GetAlbumInfoFromLastFMDTO> {
        return try {
            APIResult.Success(lastFMAPIService.getAlbumInfo(artistName, albumName, apiKey))
        } catch (_: Exception) {
            APIResult.Failure("Cannot fetch getAlbumInfo")
        }
    }
}