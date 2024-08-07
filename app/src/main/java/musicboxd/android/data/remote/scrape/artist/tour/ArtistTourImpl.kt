package musicboxd.android.data.remote.scrape.artist.tour

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import musicboxd.android.data.remote.scrape.artist.tour.model.ArtistTourDTO
import musicboxd.android.data.remote.scrape.artist.tour.model.event.EventDetailsDTO
import musicboxd.android.data.remote.scrape.artist.tour.model.event.TicketDetails
import musicboxd.android.utils.customConfig
import musicboxd.android.utils.musicBoxdLog
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
        rawString.let {
            musicBoxdLog(it)
        }
        val title = rawString.substringAfter("<meta name=\"twitter:description")
            .substringAfter("content=\"").substringBefore("\">").trim()
        val ticketUrl = rawString.substringAfter("<div class=\"AXLhOsSjT3bowfjf42kK\">")
            .substringAfter("<div class=\"GAOw5C83TxIvQixcgHaf\">").substringAfter("href=\"")
            .substringBefore("?").trim()
        val ticketSellerImgURL = rawString.substringAfter("<div class=\"Ghfp1A1T_2hwVuwIwQxs\">")
            .substringAfter("<div class=\"rfROb9KBW8Mke0Dkgged\">").substringAfter("src=\"")
            .substringBefore("\"").trim()
        val ticketSellerName = rawString.substringAfter("<div class=\"ja39xsoXRNA9NP8D6jVU\">")
            .substringAfter("<div class=\"kpx9YWtZUjvrVLG921WO\">").trim()
            .substringAfter("<p data-encore-id=\"type\"").substringAfter("\">")
            .substringBefore("</p>").trim()
        val googleMapsURL =
            "https://maps.google.com" + rawString.substringAfter("https://maps.google.com")
                .substringBefore("\"").trim()
        val dateAndTime =
            rawString.substringAfter("<meta property=\"og:description\"").substringAfter("content")
                .substringAfter(" in ").substringAfter(" on ").substringBefore("\">").trim()
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