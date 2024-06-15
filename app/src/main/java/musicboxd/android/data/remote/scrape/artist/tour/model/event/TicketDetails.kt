package musicboxd.android.data.remote.scrape.artist.tour.model.event

import kotlinx.serialization.Serializable

@Serializable
data class TicketDetails(
    val ticketSellingPlatformName: String,
    val ticketSellingPlatformImgURL: String,
    val ticketSellingPlatformStatus: String,
    val ticketBuyingUrl: String
)
