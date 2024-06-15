package musicboxd.android.data.local.events.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import musicboxd.android.data.remote.scrape.artist.tour.model.ArtistTourDTO
import musicboxd.android.data.remote.scrape.artist.tour.model.event.EventDetailsDTO

@Entity("event")
data class Event(
    @PrimaryKey
    val eventId: String,
    val artistUri: String,
    val artistTourDTO: ArtistTourDTO,
    val eventsDetailsDTO: EventDetailsDTO
)
