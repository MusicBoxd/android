package musicboxd.android.data.remote.scrape.artist.tour.model.event

import kotlinx.serialization.Serializable

@Serializable
data class EventDetailsDTO(
    val eventTitle: String,
    val eventTime: String,
    val ticketsDetails: TicketDetails,
    val googleMapsURL: String
)
