package musicboxd.android.ui.search.charts

import musicboxd.android.ui.search.charts.billboard.BillBoardChartType

sealed class ChartUIEvent {
    data class OnBillBoardChartClick(val billBoardChartType: BillBoardChartType) : ChartUIEvent()
}