package musicboxd.android.data.remote.api.spotify

import musicboxd.android.data.remote.api.APIResult
import musicboxd.android.data.remote.api.spotify.model.album.Albums
import musicboxd.android.data.remote.api.spotify.model.album.SpotifyAlbumSearchDTO
import musicboxd.android.data.remote.api.spotify.model.artist_search.SpotifyArtistSearchDTO
import musicboxd.android.data.remote.api.spotify.model.specific_artist.SpecificArtistFromSpotifyDTO
import musicboxd.android.data.remote.api.spotify.model.topTracks.TopTracksDTO
import musicboxd.android.data.remote.api.spotify.model.track.SpotifyTrackSearchDTO
import musicboxd.android.data.remote.api.spotify.model.tracklist.SpotifyAlbumTrackListDTO
import retrofit2.http.Header
import retrofit2.http.Path

interface SpotifyAPIRepo {
    suspend fun searchArtists(
        artistName: String,
        limit: String,
        authorizationToken: String
    ): APIResult<SpotifyArtistSearchDTO>

    suspend fun getArtistData(
        id: String,
        authorizationToken: String
    ): APIResult<SpecificArtistFromSpotifyDTO>

    suspend fun searchAlbums(
        albumName: String,
        limit: String,
        authorizationToken: String
    ): APIResult<SpotifyAlbumSearchDTO>

    suspend fun getTrackListOfAnAlbum(
        albumID: String,
        authorizationToken: String
    ): APIResult<SpotifyAlbumTrackListDTO>

    suspend fun searchTracks(
        trackName: String,
        limit: String,
        authorizationToken: String
    ): APIResult<SpotifyTrackSearchDTO>

    suspend fun getTopTracksOfAnArtist(
        artistId: String,
        authorizationToken: String
    ): APIResult<TopTracksDTO>

    suspend fun getAlbumsOfAnArtist(
        artistId: String,
        authorizationToken: String
    ): APIResult<Albums>

    suspend fun getATrack(
        @Path("id") trackID: String,
        @Header("Authorization") authorizationToken: String
    ): APIResult<musicboxd.android.data.remote.api.spotify.model.tracklist.Item>
}