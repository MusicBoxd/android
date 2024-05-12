package musicboxd.android.ui.search.charts

import musicboxd.android.ui.search.charts.billboard.BillBoardChartType
import musicboxd.android.ui.search.charts.spotify.SpotifyChartType

sealed class ChartUIEvent {
    data class OnBillBoardChartClick(val billBoardChartType: BillBoardChartType) : ChartUIEvent()
    data class OnSpotifyChartClick(val spotifyChartType: SpotifyChartType) : ChartUIEvent()
}