package musicboxd.android.ui.search

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import musicboxd.android.data.remote.api.APIResult
import musicboxd.android.data.remote.api.spotify.SpotifyAPIRepo
import musicboxd.android.data.remote.api.spotify.charts.SpotifyChartsAPIRepo
import musicboxd.android.data.remote.api.spotify.charts.model.SpotifyChartsDTO
import musicboxd.android.data.remote.api.spotify.model.artist_search.Item
import musicboxd.android.data.remote.api.spotify.model.token.SpotifyToken
import musicboxd.android.ui.search.charts.ChartMetaData
import musicboxd.android.ui.search.charts.ChartsScreenViewModel
import musicboxd.android.utils.customConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import javax.inject.Inject

@HiltViewModel
open class SearchScreenViewModel @Inject constructor(
    private val spotifyAPIRepo: SpotifyAPIRepo,
    private val spotifyChartsAPIRepo: SpotifyChartsAPIRepo
) :
    ChartsScreenViewModel(spotifyChartsAPIRepo) {
    private val _searchArtistsResult = MutableStateFlow(emptyList<Item>())
    val searchArtistsResult = _searchArtistsResult.asStateFlow()

    protected var spotifyToken: SpotifyToken? = SpotifyToken(
        accessToken = "",
        accessTokenExpirationTimestampMs = 0L,
        clientId = "",
        isAnonymous = false
    )

    val billBoardChartsMetaData = mutableStateListOf(
        ChartMetaData(chartName = "Artist 100", chartImgURL = mutableStateOf("")),
        ChartMetaData(chartName = "Billboard 200", chartImgURL = mutableStateOf("")),
        ChartMetaData(chartName = "Global 200", chartImgURL = mutableStateOf("")),
        ChartMetaData(chartName = "Hot 100", chartImgURL = mutableStateOf(""))
    )
    private val _spotifyChartsMetaData =
        MutableStateFlow(SpotifyChartsDTO(chartEntryViewResponses = listOf()))
    val spotifyChartsMetaData = _spotifyChartsMetaData.asStateFlow()

    private val json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }


    init {
        viewModelScope.launch(Dispatchers.Default) {
            awaitAll(async {
                spotifyToken = withContext(Dispatchers.Default) {
                    try {
                        OkHttpClient().newCall(
                            Request.Builder().get()
                                .url("https://open.spotify.com/get_access_token?reason=transport&productType=web_player")
                                .build()
                        ).execute().body?.string()
                    } catch (_: Exception) {
                        ""
                    }?.let {
                        try {
                            Json.decodeFromString<SpotifyToken>(it)
                        } catch (_: Exception) {
                            SpotifyToken(
                                accessToken = "",
                                accessTokenExpirationTimestampMs = 0,
                                clientId = "",
                                isAnonymous = false
                            )
                        }
                    }
                }
                Log.d("10MinMail", spotifyToken.toString())
            }, async {
                repeat(4) {
                    withContext(Dispatchers.IO) {
                        Jsoup.connect(
                            when (it) {
                                1 -> "https://www.billboard.com/charts/billboard-200/"
                                2 -> "https://www.billboard.com/charts/billboard-global-200/"
                                0 -> "https://www.billboard.com/charts/artist-100/"
                                else -> "https://www.billboard.com/charts/hot-100/"
                            }
                        ).customConfig().get().toString()
                            .substringAfter("<div class=\"charts-top-featured-alt")
                            .substringAfter("<img class=\"c-lazy-image__img")
                            .substringAfter("data-lazy-src=\"")
                            .substringBefore("\"").let { topImageURL ->
                                billBoardChartsMetaData[it] =
                                    billBoardChartsMetaData[it].copy(
                                        chartImgURL = mutableStateOf(
                                            topImageURL
                                        )
                                    )
                            }
                    }
                }
            }, async {
                when (val spotifyChartsData = spotifyChartsAPIRepo.getCharts()) {
                    is APIResult.Failure -> {

                    }

                    is APIResult.Success -> {
                        _spotifyChartsMetaData.emit(spotifyChartsData.data)
                    }
                }
            }, async {
                loadAllBillBoardChartsData()
            })
        }
    }

    private val _searchTracksResult = MutableStateFlow(
        emptyList<musicboxd.android.data.remote.api.spotify.model.track.Item>()
    )
    val searchTracksResult = _searchTracksResult.asStateFlow()

    private val _searchAlbumsResult =
        MutableStateFlow(emptyList<musicboxd.android.data.remote.api.spotify.model.album.Item>())
    val searchAlbumsResult = _searchAlbumsResult.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private var searchJob: Job? = null

    fun onUiEvent(searchScreenUiEvent: SearchScreenUiEvent) {
        when (searchScreenUiEvent) {
            is SearchScreenUiEvent.OnQueryChange -> {
                onQueryChange(searchScreenUiEvent.query)
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun onQueryChange(query: String) {
        viewModelScope.launch {
            _searchQuery.emit(query)
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _searchQuery.debounce(150L).collectLatest { query ->
                awaitAll(async {
                    val searchTracksData = spotifyAPIRepo.searchTracks(
                        query,
                        "10",
                        spotifyToken!!.accessToken
                    )
                    when (searchTracksData) {
                        is APIResult.Failure -> {
                            _searchTracksResult.emit(emptyList())
                        }

                        is APIResult.Success -> _searchTracksResult.emit(searchTracksData.data.tracks.items)
                    }
                }, async {
                    val searchAlbumsData = spotifyAPIRepo.searchAlbums(
                        query,
                        "10",
                        spotifyToken!!.accessToken
                    )
                    when (searchAlbumsData) {
                        is APIResult.Failure -> {
                            _searchAlbumsResult.emit(emptyList())
                        }

                        is APIResult.Success -> _searchAlbumsResult.emit(searchAlbumsData.data.albums.items.filter {
                            it.album_type.lowercase() == "album"
                        })
                    }
                }, async {
                    val searchArtistsData = spotifyAPIRepo.searchArtists(
                        query,
                        "10",
                        spotifyToken!!.accessToken
                    )
                    when (searchArtistsData) {
                        is APIResult.Failure -> {
                            _searchArtistsResult.emit(emptyList())
                        }

                        is APIResult.Success -> {
                            _searchArtistsResult.emit(searchArtistsData.data.artists.items)
                        }
                    }
                })
            }
        }
    }
}