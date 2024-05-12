package musicboxd.android.data.remote.api.spotify.charts.model

import kotlinx.serialization.Serializable

@Serializable
data class SpotifyChartsDTO(
    val chartEntryViewResponses: List<ChartEntryViewResponse>
)