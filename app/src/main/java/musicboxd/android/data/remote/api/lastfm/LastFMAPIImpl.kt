package musicboxd.android.data.remote.api.lastfm

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
    ): GetArtistInfoFromLastFM {
        return lastFMAPIService.getArtistInfo(artistName, apiKey)
    }

    override suspend fun searchArtists(artistName: String, apiKey: String): SearchArtists {
        return lastFMAPIService.searchArtists(artistName, apiKey)
    }

    override suspend fun searchTracks(trackName: String, apiKey: String): SearchTracks {
        return lastFMAPIService.searchTracks(trackName, apiKey)
    }

    override suspend fun searchAlbums(albumName: String, apiKey: String): SearchAlbumsFromLastFm {
        return lastFMAPIService.searchAlbums(albumName, apiKey)
    }

    override suspend fun getAlbumInfo(
        artistName: String,
        albumName: String,
        apiKey: String
    ): GetAlbumInfoFromLastFMDTO {
        return lastFMAPIService.getAlbumInfo(artistName, albumName, apiKey)
    }
}