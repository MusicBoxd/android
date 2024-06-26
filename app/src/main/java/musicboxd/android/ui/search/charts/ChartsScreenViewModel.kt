package musicboxd.android.ui.search.charts

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import musicboxd.android.data.remote.api.APIResult
import musicboxd.android.data.remote.api.spotify.charts.SpotifyChartsAPIRepo
import musicboxd.android.data.remote.api.spotify.charts.model.ChartEntryViewResponse
import musicboxd.android.data.remote.api.spotify.charts.model.ChartMetadata
import musicboxd.android.data.remote.api.spotify.charts.model.Dimensions
import musicboxd.android.data.remote.api.spotify.charts.model.DisplayChart
import musicboxd.android.ui.search.charts.billboard.BillBoardChartType
import musicboxd.android.ui.search.charts.billboard.model.BillBoardCharts
import musicboxd.android.ui.search.charts.billboard.model.Data
import musicboxd.android.ui.search.charts.spotify.SpotifyChartType
import musicboxd.android.utils.customConfig
import org.jsoup.Jsoup
import javax.inject.Inject

@HiltViewModel
open class ChartsScreenViewModel @Inject constructor(
    private val spotifyChartsAPIRepo: SpotifyChartsAPIRepo
) : ViewModel() {
    private var _billBoardChartData = MutableStateFlow(BillBoardCharts(date = "", data = listOf()))
    val billBoardData = _billBoardChartData.asStateFlow()

    private val _spotifyChartsData = MutableStateFlow(
        ChartEntryViewResponse(
            displayChart = DisplayChart(
                ChartMetadata(
                    alias = "", backgroundColor = "", dimensions = Dimensions(
                        chartType = "",
                        city = "",
                        country = "",
                        earliestDate = "",
                        genre = "",
                        latestDate = "",
                        recurrence = "",
                    ), entityType = "", readableTitle = "", textColor = "", uri = ""
                ), "", ""
            ), entries = listOf(), highlights = listOf()
        )
    )
    val spotifyChartsData = _spotifyChartsData.asStateFlow()

    private val _billBoardArtist100 = MutableStateFlow(BillBoardCharts(date = "", data = listOf()))

    private val _billBoard200 = MutableStateFlow(BillBoardCharts(date = "", data = listOf()))

    private val _billBoardGlobal200 = MutableStateFlow(BillBoardCharts(date = "", data = listOf()))

    private val _billBoardHot100 = MutableStateFlow(BillBoardCharts(date = "", data = listOf()))

    val chartTitle = mutableStateOf("")

    fun onUiEvent(chartUIEvent: ChartUIEvent) {
        when (chartUIEvent) {
            is ChartUIEvent.OnBillBoardChartClick -> {
                when (chartUIEvent.billBoardChartType) {
                    BillBoardChartType.ARTIST_100 -> {
                        chartTitle.value = "Artist 100"
                        viewModelScope.launch {
                            _billBoardArtist100.collectLatest {
                                _billBoardChartData.emit(it)
                            }
                        }
                    }

                    BillBoardChartType.BILLBOARD_200 -> {
                        chartTitle.value = "Billboard 200"
                        viewModelScope.launch {
                            _billBoard200.collectLatest {
                                _billBoardChartData.emit(it)
                            }
                        }
                    }

                    BillBoardChartType.GLOBAL_200 -> {
                        chartTitle.value = "Global 200"
                        viewModelScope.launch {
                            _billBoardGlobal200.collectLatest {
                                _billBoardChartData.emit(it)
                            }
                        }
                    }

                    BillBoardChartType.HOT_100 -> {
                        chartTitle.value = "Hot 100"
                        viewModelScope.launch {
                            _billBoardHot100.collectLatest {
                                _billBoardChartData.emit(it)
                            }
                        }
                    }
                }
            }

            is ChartUIEvent.OnSpotifyChartClick -> {
                viewModelScope.launch(Dispatchers.Default) {
                    val spotifyChartsData = spotifyChartsAPIRepo.getCharts()
                    when (chartUIEvent.spotifyChartType) {
                        SpotifyChartType.WEEKLY_TOP_SONGS_GLOBAL -> {
                            when (spotifyChartsData) {
                                is APIResult.Failure -> TODO()
                                is APIResult.Success -> {
                                    chartTitle.value = "Weekly Top Songs: Global"
                                    _spotifyChartsData.emit(spotifyChartsData.data.chartEntryViewResponses[0])
                                }
                            }
                        }

                        SpotifyChartType.WEEKLY_TOP_ALBUMS_GLOBAL -> {
                            when (spotifyChartsData) {
                                is APIResult.Failure -> TODO()
                                is APIResult.Success -> {
                                    chartTitle.value = "Weekly Top Albums: Global"
                                    _spotifyChartsData.emit(spotifyChartsData.data.chartEntryViewResponses[1])
                                }
                            }
                        }

                        SpotifyChartType.WEEKLY_TOP_ARTISTS_GLOBAL -> {
                            when (spotifyChartsData) {
                                is APIResult.Failure -> TODO()
                                is APIResult.Success -> {
                                    chartTitle.value = "Weekly Top Artist: Global"
                                    _spotifyChartsData.emit(spotifyChartsData.data.chartEntryViewResponses[2])
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected suspend fun loadAllBillBoardChartsData() = coroutineScope {
        awaitAll(
            async {
                _billBoard200.emit(scrapeBillBoardChartsData("https://www.billboard.com/charts/billboard-200/"))
            },
            async {
                _billBoardHot100.emit(scrapeBillBoardChartsData("https://www.billboard.com/charts/hot-100/"))
            },
            async {
                _billBoardArtist100.emit(scrapeBillBoardChartsData("https://www.billboard.com/charts/artist-100/"))
            },
            async {
                _billBoardGlobal200.emit(scrapeBillBoardChartsData("https://www.billboard.com/charts/billboard-global-200/"))
            },
        )
    }

    private suspend fun scrapeBillBoardChartsData(url: String): BillBoardCharts = coroutineScope {
        val billBoardDoc = withContext(Dispatchers.IO) {
            Jsoup.connect(url).customConfig().get()
        }
        val dateOfTheChart = billBoardDoc.toString()
            .substringAfter("<p class=\"c-tagline  a-font-primary-medium-xs u-font-size-11@mobile-max u-letter-spacing-0106 u-letter-spacing-0089@mobile-max lrv-u-line-height-copy lrv-u-text-transform-uppercase lrv-u-margin-a-00 lrv-u-padding-l-075 lrv-u-padding-l-00@mobile-max\">")
            .substringBefore("</p>")
        val itemImages = billBoardDoc.toString().substringAfter(
            "Wks on Chart"
        ).split("<div class=\"o-chart-results-list-row-container\">").map {
            it.substringAfter("<div class=\"a-chart-image lrv-u-flex lrv-u-align-items-center lrv-u-justify-content-center lrv-a-glue-parent\" style=\"background-image: url('")
                .substringBefore("')")
        }.filter {
            it.contains("https://")
        }
        val itemTitles = billBoardDoc.toString().substringAfter(
            "Wks on Chart"
        ).split("<div class=\"o-chart-results-list-row-container\">").map {
            it.substringAfter("<h3 id=\"title-of-a-story\" class=").substringAfter("\">")
                .substringBefore("</h3>").trim()
        }.filter {
            !it.contains("</")
        }
        val itemArtists = billBoardDoc.toString().substringAfter(
            "Wks on Chart"
        ).split("<div class=\"o-chart-results-list-row-container\">").map {
            it.substringAfter("<h3 id=\"title-of-a-story\" class=")
                .substringAfter("<span class=\"c-label").substringAfter("\">")
                .substringBefore("</span>").replace("&amp;", "&").trim()
        }.filter {
            it.isNotEmpty()
        }
        val positionRowItems =
            billBoardDoc.toString().substringAfter("<div class=\"chart-results-list")
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
        return@coroutineScope BillBoardCharts(date = dateOfTheChart, data = List(itemTitles.size) {
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