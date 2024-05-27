package musicboxd.android.data.remote.scrape.artist.tour

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import musicboxd.android.data.remote.scrape.artist.tour.model.ArtistTourDTO
import musicboxd.android.utils.customConfig
import org.jsoup.Jsoup

class ArtistTourImpl : ArtistTourRepo {
    override suspend fun getLatestTourDetailsFromAnArtist(artistId: String): List<ArtistTourDTO> {
        val scrapedString = withContext(Dispatchers.IO) {
            Jsoup.connect("https://open.spotify.com/artist/$artistId/concerts").customConfig().get()
        }.toString()
        val regularInfoExcludingTime = scrapedString.substringAfter("Other locations")
            .substringAfter("<div class=\"azrW1gfJbgz897iDaITJ\">")
            .split("<div class=\"ymKJ1kNm06pnfmgmATrg\">").map {
                it.substringAfter("<p class=\"Type__TypeElement")
                    .substringAfter("<time class=\"cR3tL5CgXmgwfJDDKQ2A\">")
                    .substringBefore("</p>")
            }.map {
                it.replace("</time>", ", ").trim()
            }.drop(1)
        val concertTime = scrapedString.substringAfter("Other locations")
            .substringAfter("<div class=\"azrW1gfJbgz897iDaITJ\">")
            .split("<div class=\"ymKJ1kNm06pnfmgmATrg\">").map {
                it.substringAfter("class=\"kZAjDLOBaUEUgIh0u_ar\"")
                    .substringAfter("<time class=\"jqymLdIlkUVKl8shJfKZ\"")
                    .substringAfter("datetime=\"")
                    .substringBefore("\">")
            }.dropLast(1)
        val concertsList = mutableListOf<ArtistTourDTO>()
        regularInfoExcludingTime.forEachIndexed { index, it ->
            val splitList = it.split(", ")
            concertsList.add(
                ArtistTourDTO(
                    location = splitList.last(),
                    venue = splitList[splitList.size - 2],
                    day = splitList.first(),
                    date = splitList[splitList.size - 3],
                    time = concertTime[index]
                )
            )
        }
        return concertsList.toList()
    }
}