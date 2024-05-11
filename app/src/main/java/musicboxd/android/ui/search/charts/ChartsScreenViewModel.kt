package musicboxd.android.ui.search.charts

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import musicboxd.android.ui.search.charts.billboard.BillBoardChartType
import musicboxd.android.ui.search.charts.billboard.model.BillBoardCharts
import musicboxd.android.ui.search.charts.billboard.model.Data
import musicboxd.android.utils.customConfig
import org.jsoup.Jsoup

class ChartsScreenViewModel : ViewModel() {
    private val _billBoardChartData = MutableStateFlow(BillBoardCharts(date = "", data = listOf()))
    val billBoardData = _billBoardChartData.asStateFlow()
    val chartTitle = mutableStateOf("")
    fun onUiEvent(chartUIEvent: ChartUIEvent) {
        when (chartUIEvent) {
            is ChartUIEvent.OnBillBoardChartClick -> {
                viewModelScope.launch {
                    _billBoardChartData.emit(BillBoardCharts(date = "", data = listOf()))
                    _billBoardChartData.emit(
                        scrapeBillBoardChartsData(
                            when (chartUIEvent.billBoardChartType) {
                                BillBoardChartType.ARTIST_100 -> {
                                    chartTitle.value = "Artist 100"
                                    "https://www.billboard.com/charts/artist-100/"
                                }

                                BillBoardChartType.BILLBOARD_200 -> {
                                    chartTitle.value = "Billboard 200"
                                    "https://www.billboard.com/charts/billboard-200/"
                                }

                                BillBoardChartType.GLOBAL_200 -> {
                                    chartTitle.value = "Global 200"
                                    "https://www.billboard.com/charts/billboard-global-200/"
                                }

                                BillBoardChartType.HOT_100 -> {
                                    chartTitle.value = "Hot 100"
                                    "https://www.billboard.com/charts/hot-100/"
                                }
                            }
                        )
                    )

                }
            }
        }
    }

    private suspend fun scrapeBillBoardChartsData(url: String): BillBoardCharts {
        val hot100Doc = withContext(Dispatchers.IO) {
            Jsoup.connect(url).customConfig().get()
        }
        val dateOfTheChart = hot100Doc.toString()
            .substringAfter("<p class=\"c-tagline  a-font-primary-medium-xs u-font-size-11@mobile-max u-letter-spacing-0106 u-letter-spacing-0089@mobile-max lrv-u-line-height-copy lrv-u-text-transform-uppercase lrv-u-margin-a-00 lrv-u-padding-l-075 lrv-u-padding-l-00@mobile-max\">")
            .substringBefore("</p>")
        val itemImages = hot100Doc.toString().substringAfter(
            "Wks on Chart"
        ).split("<div class=\"o-chart-results-list-row-container\">").map {
            it.substringAfter("<div class=\"a-chart-image lrv-u-flex lrv-u-align-items-center lrv-u-justify-content-center lrv-a-glue-parent\" style=\"background-image: url('")
                .substringBefore("')")
        }.filter {
            it.contains("https://")
            it.endsWith(".jpg")
        }
        val itemTitles = hot100Doc.toString().substringAfter(
            "Wks on Chart"
        ).split("<div class=\"o-chart-results-list-row-container\">").map {
            it.substringAfter("<h3 id=\"title-of-a-story\" class=").substringAfter("\">")
                .substringBefore("</h3>").trim()
        }.filter {
            !it.contains("</")
        }
        val itemArtists = hot100Doc.toString().substringAfter(
            "Wks on Chart"
        ).split("<div class=\"o-chart-results-list-row-container\">").map {
            it.substringAfter("<h3 id=\"title-of-a-story\" class=")
                .substringAfter("<span class=\"c-label").substringAfter("\">")
                .substringBefore("</span>").replace("&amp;", "&").trim()
        }.filter {
            it.isNotEmpty()
        }
        val positionRowItems =
            hot100Doc.toString().substringAfter("<div class=\"chart-results-list")
                .split("<div class=\"o-chart-results-list-row-container\">").flatMap {
                    it.substringAfter("<li class=\"lrv-u-width-100p\">")
                        .substringAfter("<li class=\"lrv-u-width-100p")
                        .split("<li class=\"o-chart-results-list__item").map {
                            it.substringAfter("<span class=\"c-label").substringAfter("\">")
                                .substringBefore("</span>").trim()
                        }
                }.filter {
                    try {
                        if (it == "-") {
                            true
                        } else {
                            it.toInt()
                            true
                        }
                    } catch (_: Exception) {
                        false
                    }
                }.chunked(3)
        return BillBoardCharts(date = dateOfTheChart, data = List(itemTitles.size) {
            Data(
                itemImgURL = try {
                    itemImages[it]
                } catch (_: Exception) {
                    ""
                },
                itemTitle = try {
                    itemTitles[it]
                } catch (_: Exception) {
                    ""
                },
                itemArtists = try {
                    itemArtists[it]
                } catch (_: Exception) {
                    ""
                },
                itemLastWeek = try {
                    positionRowItems[it][0]
                } catch (_: Exception) {
                    ""
                },
                itemPeakPosition = try {
                    positionRowItems[it][1]
                } catch (_: Exception) {
                    ""
                },
                itemWeeksOnChart = try {
                    positionRowItems[it][2]
                } catch (_: Exception) {
                    ""
                }
            )
        })
    }
}