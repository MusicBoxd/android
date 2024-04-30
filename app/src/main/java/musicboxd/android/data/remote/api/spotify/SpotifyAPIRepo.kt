package musicboxd.android.data.remote.api.spotify

import musicboxd.android.data.remote.api.spotify.model.album.SpotifyAlbumSearchDTO
import musicboxd.android.data.remote.api.spotify.model.artist.SpotifyArtistSearchDTO
import musicboxd.android.data.remote.api.spotify.model.track.SpotifyTrackSearchDTO

interface SpotifyAPIRepo {
    suspend fun searchArtists(
        artistName: String,
        limit: String,
        authorizationToken: String
    ): SpotifyArtistSearchDTO

    suspend fun searchAlbums(
        albumName: String,
        limit: String,
        authorizationToken: String
    ): SpotifyAlbumSearchDTO

    suspend fun searchTracks(
        trackName: String,
        limit: String,
        authorizationToken: String
    ): SpotifyTrackSearchDTO
}