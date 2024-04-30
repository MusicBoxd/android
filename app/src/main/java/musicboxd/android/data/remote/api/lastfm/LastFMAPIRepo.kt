package musicboxd.android.data.remote.api.lastfm

import musicboxd.android.data.remote.api.lastfm.model.searchAlbums.SearchAlbumsFromLastFm
import musicboxd.android.data.remote.api.lastfm.model.searchArtists.SearchArtists
import musicboxd.android.data.remote.api.lastfm.model.searchTracks.SearchTracks

interface LastFMAPIRepo {
    suspend fun searchArtists(artistName: String, apiKey: String): SearchArtists
    suspend fun searchTracks(trackName: String, apiKey: String): SearchTracks
    suspend fun searchAlbums(albumName: String, apiKey: String): SearchAlbumsFromLastFm
}