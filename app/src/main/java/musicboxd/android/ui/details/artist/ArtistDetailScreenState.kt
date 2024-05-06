package musicboxd.android.ui.details.artist

import musicboxd.android.data.remote.api.spotify.model.album.Albums
import musicboxd.android.data.remote.api.spotify.model.specific_artist.SpecificArtistFromSpotifyDTO
import musicboxd.android.data.remote.api.spotify.model.topTracks.TopTracksDTO

data class ArtistDetailScreenState(
    val specificArtistFromSpotifyDTO: SpecificArtistFromSpotifyDTO,
    val topTracks: TopTracksDTO,
    val albumSearchDTO: Albums
)
