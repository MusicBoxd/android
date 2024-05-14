package musicboxd.android.ui.search.charts

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.flowOf
import musicboxd.android.data.remote.api.spotify.model.album.Artist
import musicboxd.android.data.remote.api.spotify.model.album.ExternalUrlsX
import musicboxd.android.data.remote.api.spotify.model.artist_search.Artists
import musicboxd.android.data.remote.api.spotify.model.artist_search.ExternalUrls
import musicboxd.android.data.remote.api.spotify.model.artist_search.Followers
import musicboxd.android.data.remote.api.spotify.model.artist_search.Image
import musicboxd.android.data.remote.api.spotify.model.artist_search.Item
import musicboxd.android.ui.common.CoilImage
import musicboxd.android.ui.details.DetailsViewModel
import musicboxd.android.ui.details.album.AlbumDetailScreenState
import musicboxd.android.ui.navigation.NavigationRoutes
import musicboxd.android.ui.search.charts.billboard.model.Data

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartsScreen(
    detailsViewModel: DetailsViewModel,
    navController: NavController
) {
    val billBoardChartsData = detailsViewModel.billBoardData.collectAsStateWithLifecycle()
    val spotifyChartsData = detailsViewModel.spotifyChartsData.collectAsStateWithLifecycle()
    val topAppBarScrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(topAppBarScrollBehaviour.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(scrollBehavior = topAppBarScrollBehaviour, title = {
                Text(
                    text = detailsViewModel.chartTitle.value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 15.dp)
                )
            })
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Filled.DateRange, contentDescription = "")
                        Spacer(modifier = Modifier.width(15.dp))
                        Text(
                            text = billBoardChartsData.value.date,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
            if ((detailsViewModel.chartTitle.value.lowercase()
                    .contains("weekly")
                        && spotifyChartsData.value.entries.isEmpty()) || (!detailsViewModel.chartTitle.value.lowercase()
                    .contains("weekly")
                        && billBoardChartsData.value.data.isEmpty())
            ) {
                item {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                        LinearProgressIndicator(modifier = Modifier.padding(65.dp))
                    }
                }
                return@LazyColumn
            }
            if (detailsViewModel.chartTitle.value.lowercase().contains("weekly")) {
                itemsIndexed(spotifyChartsData.value.entries) { index, data ->
                    ChartItem(
                        artists = when {
                            detailsViewModel.chartTitle.value.lowercase()
                                .contains("songs") -> {
                                Artists(href = "", items = data.trackMetadata.artists.map {
                                    Item(
                                        external_urls = ExternalUrls(
                                            spotify = "",
                                        ),
                                        followers = Followers(
                                            total = 0,
                                        ),
                                        genres = listOf(),
                                        href = "",
                                        id = it.spotifyUri.substringAfter("spotify:artist:"),
                                        images = listOf(),
                                        name = it.name,
                                        popularity = 0,
                                        type = "",
                                        uri = it.spotifyUri
                                    )
                                }, limit = 0, offset = 0, total = 0)
                            }

                            else -> {
                                Artists(href = "", items = data.albumMetadata.artists.map {
                                    Item(
                                        external_urls = ExternalUrls(
                                            spotify = "",
                                        ),
                                        followers = Followers(
                                            total = 0,
                                        ),
                                        genres = listOf(),
                                        href = "",
                                        id = it.spotifyUri.substringAfter("spotify:artist:"),
                                        images = listOf(),
                                        name = it.name,
                                        popularity = 0,
                                        type = "",
                                        uri = it.spotifyUri
                                    )
                                }, limit = 0, offset = 0, total = 0)
                            }
                        },
                        onItemClick = {
                            when {
                                detailsViewModel.chartTitle.value.lowercase()
                                    .contains("songs") -> {
                                    detailsViewModel.albumScreenState = AlbumDetailScreenState(
                                        covertArtImgUrl = flowOf(),
                                        albumImgUrl = "",
                                        albumTitle = "",
                                        artists = listOf(),
                                        albumWiki = flowOf(),
                                        releaseDate = "",
                                        trackList = flowOf(),
                                        itemType = ""
                                    )
                                    detailsViewModel.albumScreenState =
                                        detailsViewModel.albumScreenState.copy(albumImgUrl = data.trackMetadata.displayImageUri)
                                    detailsViewModel.loadATrack(
                                        data.trackMetadata.trackUri.substringAfter(
                                            "spotify:track:"
                                        )
                                    )
                                    detailsViewModel.loadReleaseDate(
                                        isTrack = true, data.trackMetadata.trackUri.substringAfter(
                                            "spotify:track:"
                                        ), ""
                                    )
                                    navController.navigate(NavigationRoutes.ALBUM_DETAILS.name)
                                }

                                detailsViewModel.chartTitle.value.lowercase()
                                    .contains("albums") -> {
                                    detailsViewModel.albumScreenState = AlbumDetailScreenState(
                                        covertArtImgUrl = flowOf(),
                                        albumImgUrl = data.albumMetadata.displayImageUri,
                                        albumTitle = data.albumMetadata.albumName,
                                        artists = data.albumMetadata.artists.map {
                                            Artist(
                                                external_urls = ExternalUrlsX(
                                                    spotify = it.externalUrl,
                                                ),
                                                href = "",
                                                id = it.spotifyUri.substringAfter("spotify:artist:"),
                                                name = it.name,
                                                type = "Track",
                                                uri = it.spotifyUri
                                            )
                                        },
                                        albumWiki = flowOf(),
                                        releaseDate = data.albumMetadata.releaseDate,
                                        trackList = flowOf(),
                                        itemType = "Album"
                                    )
                                    detailsViewModel.loadAlbumInfo(
                                        albumID = data.albumMetadata.albumUri.substringAfter("spotify:album:"),
                                        albumName = data.albumMetadata.albumName,
                                        artistID = data.albumMetadata.artists.random().spotifyUri.substringAfter(
                                            "spotify:artist:"
                                        ),
                                        artistName = data.albumMetadata.artists.random().name
                                    )
                                    detailsViewModel.loadReleaseDate(
                                        isTrack = false,
                                        albumID = data.albumMetadata.albumUri.substringAfter(
                                            "spotify:album:"
                                        ),
                                        trackID = ""
                                    )
                                    navController.navigate(NavigationRoutes.ALBUM_DETAILS.name)
                                }

                                else -> {
                                    detailsViewModel.artistInfo.value = Item(
                                        external_urls = ExternalUrls(
                                            spotify = "",
                                        ),
                                        followers = Followers(
                                            total = 0,
                                        ),
                                        genres = listOf(),
                                        href = "",
                                        id = "",
                                        images = listOf(
                                            Image(
                                                height = 0,
                                                url = data.artistMetadata.displayImageUri,
                                                width = 0
                                            )
                                        ),
                                        name = "",
                                        popularity = 0,
                                        type = "",
                                        uri = ""
                                    )
                                    detailsViewModel.loadArtistInfo(
                                        data.artistMetadata.artistUri.substringAfter("spotify:artist:"),
                                        data.artistMetadata.artistName,
                                        loadArtistMetaData = true
                                    )
                                    navController.navigate(NavigationRoutes.ARTIST_DETAILS.name)
                                }
                            }
                        },
                        onArtistClick = {
                            detailsViewModel.artistInfo.value = it
                            detailsViewModel.loadArtistInfo(
                                it.id,
                                it.name,
                                loadArtistMetaData = true
                            )
                            navController.navigate(NavigationRoutes.ARTIST_DETAILS.name)
                        },
                        data = when {
                            detailsViewModel.chartTitle.value.lowercase()
                                .contains("songs") -> Data(
                                itemImgURL = data.trackMetadata.displayImageUri,
                                itemTitle = data.trackMetadata.trackName,
                                itemArtists = data.trackMetadata.artists.joinToString { it.name },
                                itemLastWeek = data.chartEntryData.previousRank.toString(),
                                itemPeakPosition = data.chartEntryData.peakRank.toString(),
                                itemWeeksOnChart = data.chartEntryData.consecutiveAppearancesOnChart.toString()
                            )

                            detailsViewModel.chartTitle.value.lowercase()
                                .contains("albums") -> Data(
                                itemImgURL = data.albumMetadata.displayImageUri,
                                itemTitle = data.albumMetadata.albumName,
                                itemArtists = data.albumMetadata.artists.joinToString { it.name },
                                itemLastWeek = data.chartEntryData.previousRank.toString(),
                                itemPeakPosition = data.chartEntryData.peakRank.toString(),
                                itemWeeksOnChart = data.chartEntryData.consecutiveAppearancesOnChart.toString()
                            )

                            else -> Data(
                                itemImgURL = data.artistMetadata.displayImageUri,
                                itemTitle = data.artistMetadata.artistName,
                                itemArtists = "",
                                itemLastWeek = data.chartEntryData.previousRank.toString(),
                                itemPeakPosition = data.chartEntryData.peakRank.toString(),
                                itemWeeksOnChart = data.chartEntryData.consecutiveAppearancesOnChart.toString()
                            )
                        },
                        index = index,
                        isArtist = detailsViewModel.chartTitle.value.contains("Artist")
                    )
                }
            } else {
                itemsIndexed(billBoardChartsData.value.data) { index, data ->
                    ChartItem(
                        data = data,
                        index = index,
                        isArtist = detailsViewModel.chartTitle.value.contains("Artist")
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChartItem(
    data: Data,
    index: Int,
    isArtist: Boolean,
    onItemClick: () -> Unit = {},
    onArtistClick: (item: Item) -> Unit = {},
    artists: Artists = Artists(href = "", items = listOf(), limit = 0, offset = 0, total = 0)
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(
                onClick = { onItemClick() },
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = if (data.itemImgURL.contains("i.scdn.co/image")) LocalIndication.current else null
            )
            .padding(start = 15.dp, end = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        Text(
            text = (index + 1).toString(),
            style = MaterialTheme.typography.titleLarge
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CoilImage(
                    imgUrl = data.itemImgURL,
                    modifier = Modifier
                        .size(75.dp)
                        .clip(if (isArtist) CircleShape else RoundedCornerShape(5.dp)),
                    contentDescription = ""
                )
                if (isArtist) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = data.itemTitle,
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ChartPositionItem(title = "W.O.C", value = data.itemWeeksOnChart)
                            Spacer(modifier = Modifier.width(10.dp))
                            ChartPositionItem(title = "P.P", value = data.itemPeakPosition)
                            Spacer(modifier = Modifier.width(10.dp))
                            ChartPositionItem(title = "L.W", value = data.itemLastWeek)
                        }
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ChartPositionItem(title = "W.O.K", value = data.itemWeeksOnChart)
                        ChartPositionItem(title = "P.P", value = data.itemPeakPosition)
                        ChartPositionItem(title = "L.W", value = data.itemLastWeek)
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            if (!isArtist) {
                Text(
                    text = data.itemTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            if (!isArtist) {
                Spacer(modifier = Modifier.height(2.dp))
                if (artists.items.isNotEmpty()) {
                    FlowRow {
                        artists.items.forEach {
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.clickable(
                                    onClick = { onArtistClick(it) })
                            )
                            if (artists.items.last() != it)
                                Text(
                                    text = ", ",
                                    style = MaterialTheme.typography.titleSmall
                                )
                        }
                    }
                } else {
                    Text(
                        text = data.itemArtists,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        color = LocalContentColor.current.copy(0.25f)
    )
}

@Composable
private fun ChartPositionItem(title: String, value: String) {
    Spacer(modifier = Modifier.width(5.dp))
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = LocalContentColor.current.copy(0.8f)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp
        )
    }
}