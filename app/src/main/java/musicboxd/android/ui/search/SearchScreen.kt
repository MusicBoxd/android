package musicboxd.android.ui.search

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import musicboxd.android.R
import musicboxd.android.ui.common.CoilImage
import musicboxd.android.ui.common.fadedEdges
import musicboxd.android.ui.details.DetailsViewModel
import musicboxd.android.ui.search.charts.ChartCard
import musicboxd.android.ui.theme.MusicBoxdTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun SearchScreen(
    searchScreenViewModel: SearchScreenViewModel,
    navController: NavController,
    detailsViewModel: DetailsViewModel
) {
    val searchQuery = searchScreenViewModel.searchQuery.collectAsStateWithLifecycle()
    val isSearchActive = rememberSaveable {
        mutableStateOf(false)
    }
    val billBoardMetaData = searchScreenViewModel.billBoardChartsMetaData
    val spotifyChartsData =
        searchScreenViewModel.spotifyChartsMetaData.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState()
    MusicBoxdTheme {
        LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.fillMaxSize()) {
            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                ProvideTextStyle(value = MaterialTheme.typography.titleSmall) {
                    SearchBar(
                        colors = SearchBarDefaults.colors(dividerColor = Color.Transparent),
                        leadingIcon = {
                            IconButton(onClick = {
                                isSearchActive.value = false;searchScreenViewModel.onUiEvent(
                                SearchScreenUiEvent.OnQueryChange("")
                            )
                            }) {
                                Icon(
                                    imageVector = if (isSearchActive.value) Icons.Default.ArrowBack else Icons.Default.Search,
                                    contentDescription = if (isSearchActive.value) "Arrow Back Icon" else "Search Icon"
                                )
                            }
                        },
                        trailingIcon = {
                            if (isSearchActive.value) {
                                IconButton(onClick = {
                                    if (searchQuery.value.isNotBlank()) searchScreenViewModel.onUiEvent(
                                        SearchScreenUiEvent.OnQueryChange("")
                                    ) else isSearchActive.value = false
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Cancel,
                                        contentDescription = "Cancel Icon"
                                    )
                                }
                            }
                        },
                        placeholder = {
                            Text(
                                text = "Search MusicBoxd",
                                style = MaterialTheme.typography.titleSmall
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(if (isSearchActive.value) 0.dp else 15.dp)
                            .animateContentSize(),
                        query = searchQuery.value,
                        onQueryChange = {
                            searchScreenViewModel.onUiEvent(SearchScreenUiEvent.OnQueryChange(it))
                        },
                        onSearch = {

                        },
                        active = isSearchActive.value,
                        onActiveChange = {
                            isSearchActive.value = it
                        },
                        content = {
                            SearchContent(
                                searchScreenViewModel = searchScreenViewModel,
                                navController,
                                detailsViewModel
                            )
                        }
                    )
                }
            }
            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(15.dp))
                    Image(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .border(1.dp, LocalContentColor.current, CircleShape),
                        painter = painterResource(id = R.drawable.billboard_logo),
                        contentDescription = ""
                    )
                    Text(text = " • Charts", style = MaterialTheme.typography.titleSmall)
                }
            }
            itemsIndexed(billBoardMetaData) { index, chartMetaData ->
                ChartCard(
                    text = chartMetaData.chartName,
                    imgURL = chartMetaData.chartImgURL.value,
                    index
                )
            }
            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                FilledTonalButton(
                    onClick = { },
                    modifier = Modifier.padding(
                        start = 15.dp,
                        end = 15.dp,
                        bottom = 15.dp,
                        top = 7.5.dp
                    )
                ) {
                    Text(
                        text = "View All BillBoard Charts",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }

            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(15.dp))
                    Image(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape),
                        painter = painterResource(id = R.drawable.spotify_logo),
                        contentDescription = ""
                    )
                    Text(text = " • Charts", style = MaterialTheme.typography.titleSmall)
                }
            }
            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(15.dp)
                        .clip(CardDefaults.shape)
                ) {
                    Box(Modifier.fillMaxSize()) {
                        HorizontalPager(state = pagerState, count = 3) {
                            Box(Modifier.fillMaxSize()) {
                                CoilImage(
                                    imgUrl = try {
                                        spotifyChartsData.value.chartEntryViewResponses[it].highlights[0].displayImageUri
                                    } catch (_: Exception) {
                                        ""
                                    },
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .fadedEdges(MaterialTheme.colorScheme)
                                        .fadedEdges(MaterialTheme.colorScheme),
                                    contentDescription = ""
                                )
                                Text(
                                    text = spotifyChartsData.value.chartEntryViewResponses[it].highlights[0].text,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(15.dp)
                                        .align(Alignment.BottomStart),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Start
                                )
                            }
                        }
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                        )
                    }
                }
            }
            itemsIndexed(spotifyChartsData.value.chartEntryViewResponses) { index, data ->
                ChartCard(
                    text = data.displayChart.chartMetadata.readableTitle, imgURL = when (index) {
                        0 -> data.entries[0].trackMetadata.displayImageUri
                        1 -> data.entries[0].albumMetadata.displayImageUri
                        else -> data.entries[0].artistMetadata.displayImageUri
                    }, index = index
                )
            }
            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                Spacer(modifier = Modifier.height(150.dp))
            }
        }
    }
}