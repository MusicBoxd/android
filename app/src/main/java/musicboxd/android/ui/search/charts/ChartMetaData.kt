package musicboxd.android.ui.search.charts

import androidx.compose.runtime.MutableState

data class ChartMetaData(
    val chartName: String,
    val chartImgURL: MutableState<String>
)
