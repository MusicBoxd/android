package musicboxd.android.data.remote.api.lastfm

import musicboxd.android.data.remote.api.APIResult
import musicboxd.android.data.remote.api.lastfm.model.getAlbumInfo.GetAlbumInfoFromLastFMDTO
import musicboxd.android.data.remote.api.lastfm.model.getArtistInfo.GetArtistInfoFromLastFM
import musicboxd.android.data.remote.api.lastfm.model.searchAlbums.SearchAlbumsFromLastFm
import musicboxd.android.data.remote.api.lastfm.model.searchArtists.SearchArtists
import musicboxd.android.data.remote.api.lastfm.model.searchTracks.SearchTracks

interface LastFMAPIRepo {
    suspend fun getArtistInfo(
        artistName: String,
        apiKey: String
    ): APIResult<GetArtistInfoFromLastFM>

    suspend fun searchArtists(artistName: String, apiKey: String): APIResult<SearchArtists>
    suspend fun searchTracks(trackName: String, apiKey: String): APIResult<SearchTracks>
    suspend fun searchAlbums(albumName: String, apiKey: String): APIResult<SearchAlbumsFromLastFm>
    suspend fun getAlbumInfo(
        artistName: String,
        albumName: String,
        apiKey: String
    ): APIResult<GetAlbumInfoFromLastFMDTO>
}