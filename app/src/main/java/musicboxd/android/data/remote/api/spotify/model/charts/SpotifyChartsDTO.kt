package musicboxd.android.data.remote.api.spotify.model.charts

import kotlinx.serialization.Serializable

@Serializable
data class SpotifyChartsDTO(
    val chartEntryViewResponses: List<ChartEntryViewResponse>
)