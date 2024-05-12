package musicboxd.android.data.remote.api.spotify.charts.model

import kotlinx.serialization.Serializable

@Serializable

data class DisplayChart(
    val chartMetadata: ChartMetadata,
    val date: String,
    val description: String
)