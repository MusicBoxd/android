package musicboxd.android.data.remote.api.spotify.model.charts

import kotlinx.serialization.Serializable

@Serializable

data class ChartEntryViewResponse(
    val displayChart: DisplayChart,
    val entries: List<Entry>,
    val highlights: List<Highlight>
)