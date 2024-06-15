package musicboxd.android.ui.drafts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FeaturedPlayList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import musicboxd.android.data.remote.api.spotify.model.album.Artist
import musicboxd.android.data.remote.api.spotify.model.album.ExternalUrlsX
import musicboxd.android.ui.common.CoilImage
import musicboxd.android.ui.details.DetailsViewModel
import musicboxd.android.ui.details.album.AlbumDetailScreenState
import musicboxd.android.ui.lists.CreateANewListScreenViewModel
import musicboxd.android.ui.navigation.NavigationRoutes

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DraftsScreen(
    detailsViewModel: DetailsViewModel,
    navController: NavController,
    createANewListScreenViewModel: CreateANewListScreenViewModel
) {
    val pagerState = rememberPagerState()
    val draftsScreenVM: DraftsScreenVM = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()
    val localReviews = draftsScreenVM.localReviews.collectAsStateWithLifecycle()
    val localLists = draftsScreenVM.localLists.collectAsStateWithLifecycle()
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {
            Text(
                text = "Drafts",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                fontSize = 24.sp
            )
        })
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            TabRow(selectedTabIndex = pagerState.currentPage) {
                listOf(
                    "Reviews", "Lists"
                ).forEachIndexed { index, item ->
                    Tab(selected = pagerState.currentPage == index, onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }) {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(15.dp),
                            color = if (pagerState.currentPage == index) TabRowDefaults.contentColor else MaterialTheme.colorScheme.onSurface.copy(
                                0.70f
                            )
                        )
                    }
                }
            }
            HorizontalPager(count = 2, state = pagerState) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    when {
                        it == 0 -> {
                            items(localReviews.value) {
                                Spacer(
                                    modifier = Modifier
                                        .height(15.dp)
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surface)
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 15.dp, end = 15.dp)
                                        .clickable {
                                            detailsViewModel.albumScreenState =
                                                AlbumDetailScreenState(
                                                    covertArtImgUrl = flow { },
                                                    albumImgUrl = it.releaseImgUrl,
                                                    albumTitle = it.releaseName,
                                                    artists = it.artists.map {
                                                        Artist(
                                                            external_urls = ExternalUrlsX(spotify = ""),
                                                            href = "",
                                                            id = it.id,
                                                            name = it.name,
                                                            type = "",
                                                            uri = it.uri
                                                        )
                                                    },
                                                    albumWiki = flow { },
                                                    releaseDate = "",
                                                    trackList = flow { },
                                                    itemType = it.releaseType,
                                                    itemUri = it.spotifyUri
                                                )
                                            navController.navigate(NavigationRoutes.CREATE_A_NEW_REVIEW.name)
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CoilImage(
                                        imgUrl = it.releaseImgUrl,
                                        modifier = Modifier
                                            .size(65.dp)
                                            .clip(RoundedCornerShape(15.dp)),
                                        contentDescription = ""
                                    )
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Column {
                                        Text(
                                            text = it.reviewTitle,
                                            fontSize = 18.sp,
                                            style = MaterialTheme.typography.titleLarge,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(end = 20.dp),
                                            fontWeight = FontWeight.Black,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.height(5.dp))
                                        Text(
                                            text = it.reviewContent,
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(end = 20.dp),
                                            color = LocalContentColor.current.copy(0.8f),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }

                        else -> items(localLists.value) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 15.dp, top = 15.dp, end = 15.dp)
                                    .clickable {
                                        createANewListScreenViewModel.loadASpecificExistingLocalListDraft(
                                            it.localId
                                        ) {
                                            navController.navigate(NavigationRoutes.CREATE_A_NEW_LIST.name)
                                        }
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                androidx.compose.material3.Icon(
                                    imageVector = Icons.Default.FeaturedPlayList,
                                    modifier = Modifier.size(32.dp),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(text = it.nameOfTheList)
                            }
                        }
                    }
                }
            }
        }
    }
}