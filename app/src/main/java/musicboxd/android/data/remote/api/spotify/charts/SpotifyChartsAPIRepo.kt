package musicboxd.android.data.remote.api.spotify.charts

import musicboxd.android.data.remote.api.APIResult
import musicboxd.android.data.remote.api.spotify.charts.model.SpotifyChartsDTO

interface SpotifyChartsAPIRepo {
    suspend fun getCharts(): APIResult<SpotifyChartsDTO>

}