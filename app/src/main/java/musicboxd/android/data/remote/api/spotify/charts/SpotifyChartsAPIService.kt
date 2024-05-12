package musicboxd.android.data.remote.api.spotify.charts

import musicboxd.android.data.remote.api.spotify.charts.model.SpotifyChartsDTO
import retrofit2.http.GET

interface SpotifyChartsAPIService {
    @GET("charts")
    suspend fun getCharts(): SpotifyChartsDTO
}