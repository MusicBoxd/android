package musicboxd.android.data.remote.scrape.artist.tour

import musicboxd.android.data.remote.scrape.artist.tour.model.ArtistTourDTO

interface ArtistTourRepo {
    suspend fun getLatestTourDetailsFromAnArtist(artistId: String): List<ArtistTourDTO>
}