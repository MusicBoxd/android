package musicboxd.android.data.remote.api.spotify

import musicboxd.android.data.remote.api.spotify.model.album.Albums
import musicboxd.android.data.remote.api.spotify.model.album.SpotifyAlbumSearchDTO
import musicboxd.android.data.remote.api.spotify.model.artist_search.SpotifyArtistSearchDTO
import musicboxd.android.data.remote.api.spotify.model.specific_artist.SpecificArtistFromSpotifyDTO
import musicboxd.android.data.remote.api.spotify.model.topTracks.TopTracksDTO
import musicboxd.android.data.remote.api.spotify.model.track.SpotifyTrackSearchDTO
import musicboxd.android.data.remote.api.spotify.model.tracklist.SpotifyAlbumTrackListDTO

interface SpotifyAPIRepo {
    suspend fun searchArtists(
        artistName: String,
        limit: String,
        authorizationToken: String
    ): SpotifyArtistSearchDTO

    suspend fun getArtistData(
        id: String,
        authorizationToken: String
    ): SpecificArtistFromSpotifyDTO

    suspend fun searchAlbums(
        albumName: String,
        limit: String,
        authorizationToken: String
    ): SpotifyAlbumSearchDTO

    suspend fun getTrackListOfAnAlbum(
        albumID: String,
        authorizationToken: String
    ): SpotifyAlbumTrackListDTO

    suspend fun searchTracks(
        trackName: String,
        limit: String,
        authorizationToken: String
    ): SpotifyTrackSearchDTO

    suspend fun getTopTracksOfAnArtist(
        artistId: String,
        authorizationToken: String
    ): TopTracksDTO

    suspend fun getAlbumsOfAnArtist(
        artistId: String,
        authorizationToken: String
    ): Albums
}