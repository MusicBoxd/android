package musicboxd.android.data.remote.api.wikipedia.model

import kotlinx.serialization.Serializable

@Serializable
data class WikipediaDTO(
    val content_urls: ContentUrls,
    val extract: String,
    val title: String
)