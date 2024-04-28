package musicboxd.android.data.remote.api.lastfm

import musicboxd.android.data.remote.api.lastfm.model.searchAlbums.SearchAlbums
import musicboxd.android.data.remote.api.lastfm.model.searchArtists.SearchArtists
import musicboxd.android.data.remote.api.lastfm.model.searchTracks.SearchTracks
import javax.inject.Inject

class LastFMAPIImpl @Inject constructor(private val lastFMAPIService: LastFMAPIService) :
    LastFMAPIRepo {
    override suspend fun searchArtists(artistName: String, apiKey: String): SearchArtists {
        return lastFMAPIService.searchArtists(artistName, apiKey)
    }

    override suspend fun searchTracks(trackName: String, apiKey: String): SearchTracks {
        return lastFMAPIService.searchTracks(trackName, apiKey)
    }

    override suspend fun searchAlbums(albumName: String, apiKey: String): SearchAlbums {
        return lastFMAPIService.searchAlbums(albumName, apiKey)
    }
}