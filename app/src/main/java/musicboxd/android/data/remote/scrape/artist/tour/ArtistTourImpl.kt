package musicboxd.android.data.remote.scrape.artist.tour

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import musicboxd.android.data.remote.scrape.artist.tour.model.ArtistTourDTO
import musicboxd.android.data.remote.scrape.artist.tour.model.event.EventDetailsDTO
import musicboxd.android.data.remote.scrape.artist.tour.model.event.TicketDetails
import musicboxd.android.utils.customConfig
import org.jsoup.Jsoup

class ArtistTourImpl : ArtistTourRepo {
    override suspend fun getLatestTourDetailsFromAnArtist(artistId: String): List<ArtistTourDTO> {
        val scrapedString = withContext(Dispatchers.IO) {
            Jsoup.connect("https://open.spotify.com/artist/$artistId/concerts").customConfig().get()
        }.toString().substringAfter("Other locations")
            .substringAfter("<div class=\"azrW1gfJbgz897iDaITJ\">")

        val regularInfoExcludingTime =
            scrapedString.split("<div class=\"ymKJ1kNm06pnfmgmATrg\">").map {
                it.substringAfter("<p class=\"Type__TypeElement")
                    .substringAfter("<time class=\"cR3tL5CgXmgwfJDDKQ2A\">")
                    .substringBefore("</p>")
            }.map {
                it.replace("</time>", ", ").trim()
            }.drop(1)

        val concertTime = scrapedString
            .split("<div class=\"ymKJ1kNm06pnfmgmATrg\">").map {
                it.substringAfter("class=\"kZAjDLOBaUEUgIh0u_ar\"")
                    .substringAfter("<time class=\"jqymLdIlkUVKl8shJfKZ\"")
                    .substringAfter("datetime=\"")
                    .substringBefore("\">")
            }.dropLast(1)

        val href = scrapedString.substringAfter("<ul class=\"TRxg_nR4uZzjqs_s6m1N\">")
            .split("class=\"kZAjDLOBaUEUgIh0u_ar\"").map {
                it.substringAfter("href=\"").substringBefore("\">")
            }.drop(1)
        val concertsList = mutableListOf<ArtistTourDTO>()
        regularInfoExcludingTime.forEachIndexed { index, it ->
            val splitList = it.split(", ")
            concertsList.add(
                ArtistTourDTO(
                    location = splitList.last(),
                    venue = splitList[splitList.size - 2],
                    day = splitList.first(),
                    date = splitList[splitList.size - 3],
                    time = concertTime[index],
                    href = href[index]
                )
            )
        }
        return concertsList.toList()
    }

    override suspend fun getEventDetails(eventId: String): EventDetailsDTO {
        val rawString = withContext(Dispatchers.IO) {
            Jsoup.connect("https://open.spotify.com/concert/$eventId").customConfig().get()
        }.toString()
        val title = rawString.substringAfter("<meta name=\"twitter:description")
            .substringAfter("content=\"").substringBefore("\">").trim()
        val ticketUrl = rawString.substringAfter("<div class=\"muv9sEjLTV7JPw7bx6CD\">")
            .substringAfter("<div class=\"YA0OdjctuKd3c25S65Fj\">").substringAfter("href=\"")
            .substringBefore("\"").trim()
        val ticketSellerImgURL = rawString.substringAfter("<div class=\"ja39xsoXRNA9NP8D6jVU\">")
            .substringAfter("<div class=\"kpx9YWtZUjvrVLG921WO\">").substringAfter("src=\"")
            .substringBefore("\"").trim()
        val ticketSellerName = rawString.substringAfter("<div class=\"ja39xsoXRNA9NP8D6jVU\">")
            .substringAfter("<div class=\"kpx9YWtZUjvrVLG921WO\">").trim()
            .substringAfter("<p data-encore-id=\"type\"").substringAfter("\">")
            .substringBefore("</p>").trim()
        val googleMapsURL =
            "https://maps.google.com" + rawString.substringAfter("https://maps.google.com")
                .substringBefore("\"").trim()
        val dateAndTime =
            rawString.substringAfter("class=\"Type__TypeElement-sc-goli3j-0 cylFhp\">")
                .substringBefore("</div>").trim()
        return EventDetailsDTO(
            eventTitle = title,
            eventTime = dateAndTime,
            ticketsDetails = TicketDetails(
                ticketSellingPlatformName = ticketSellerName,
                ticketSellingPlatformImgURL = ticketSellerImgURL,
                ticketSellingPlatformStatus = "On sale",
                ticketBuyingUrl = ticketUrl
            ),
            googleMapsURL = googleMapsURL
        )
    }
}