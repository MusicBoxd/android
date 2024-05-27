package musicboxd.android.data.remote.scrape.artist.tour

import musicboxd.android.data.remote.scrape.artist.tour.model.ArtistTourDTO
import musicboxd.android.data.remote.scrape.artist.tour.model.event.EventDetailsDTO

interface ArtistTourRepo {
    suspend fun getLatestTourDetailsFromAnArtist(artistId: String): List<ArtistTourDTO>

    suspend fun getEventDetails(eventId: String): EventDetailsDTO
}