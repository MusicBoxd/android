package musicboxd.android.data.remote.api.spotify.model.charts

import kotlinx.serialization.Serializable

@Serializable

data class DisplayChart(
    val chartMetadata: ChartMetadata,
    val date: String,
    val description: String
)