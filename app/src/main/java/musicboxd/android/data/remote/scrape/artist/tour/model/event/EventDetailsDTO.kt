package musicboxd.android.data.remote.scrape.artist.tour.model.event

data class EventDetailsDTO(
    val eventTitle: String,
    val eventTime: String,
    val ticketsDetails: TicketDetails,
    val googleMapsURL: String
)
