package musicboxd.android.data.remote.api.wikipedia

import androidx.annotation.Keep
import musicboxd.android.data.remote.api.wikipedia.model.WikipediaDTO
import retrofit2.http.GET
import retrofit2.http.Path

@Keep
interface WikipediaAPIService {
    @GET("page/summary/{query}")
    suspend fun getSummary(@Path("query") query: String): WikipediaDTO
}