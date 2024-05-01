package musicboxd.android.data.remote.api.wikipedia

import musicboxd.android.data.remote.api.wikipedia.model.WikipediaDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface WikipediaAPIService {
    @GET("page/summary/{query}")
    suspend fun getSummary(@Path("query") query: String): WikipediaDTO
}