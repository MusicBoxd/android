package musicboxd.android.data.remote.api.spotify.charts

import musicboxd.android.data.remote.api.APIResult
import musicboxd.android.data.remote.api.spotify.charts.model.SpotifyChartsDTO
import javax.inject.Inject

class SpotifyChartsAPIImpl @Inject constructor(private val spotifyChartsAPIService: SpotifyChartsAPIService) :
    SpotifyChartsAPIRepo {
    override suspend fun getCharts(): APIResult<SpotifyChartsDTO> {
        return try {
            APIResult.Success(spotifyChartsAPIService.getCharts())
        } catch (_: Exception) {
            APIResult.Failure("couldn't fetch getCharts")
        }
    }
}