package musicboxd.android.data.remote.api.wikipedia

import musicboxd.android.data.remote.api.wikipedia.model.WikipediaDTO

interface WikipediaAPIRepo {
    suspend fun getSummary(query: String): WikipediaDTO
}