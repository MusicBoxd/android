package musicboxd.android.data.remote.api.wikipedia

import musicboxd.android.data.remote.api.wikipedia.model.ContentUrls
import musicboxd.android.data.remote.api.wikipedia.model.Mobile
import musicboxd.android.data.remote.api.wikipedia.model.WikipediaDTO
import javax.inject.Inject

class WikipediaAPIImpl @Inject constructor(private val wikipediaAPIService: WikipediaAPIService) :
    WikipediaAPIRepo {
    override suspend fun getSummary(query: String): WikipediaDTO {
        return try {
            wikipediaAPIService.getSummary(query)
        } catch (_: Exception) {
            WikipediaDTO(
                content_urls = ContentUrls(
                    mobile = Mobile(edit = "", page = "", revisions = "", talk = ""),
                ), extract = "", title = ""
            )
        }
    }
}