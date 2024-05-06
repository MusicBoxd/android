package musicboxd.android.ui.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import musicboxd.android.data.remote.api.spotify.model.album.Albums
import musicboxd.android.data.remote.api.spotify.model.specific_artist.SpecificArtistFromSpotifyDTO
import musicboxd.android.data.remote.api.spotify.model.topTracks.TopTracksDTO
import musicboxd.android.ui.common.AlbumxTrackHorizontalPreview
import musicboxd.android.ui.common.ArtistHorizontalPreview
import musicboxd.android.ui.details.DetailsViewModel
import musicboxd.android.ui.details.album.AlbumDetailScreenState
import musicboxd.android.ui.details.artist.ArtistDetailScreenState
import musicboxd.android.ui.navigation.NavigationRoutes

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun SearchContent(
    searchScreenViewModel: SearchScreenViewModel,
    navController: NavController,
    detailsViewModel: DetailsViewModel
) {
    val searchArtistsResult =
        searchScreenViewModel.searchArtistsResult.collectAsStateWithLifecycle()
    val searchTracksResult = searchScreenViewModel.searchTracksResult.collectAsStateWithLifecycle()
    val searchAlbumsResult = searchScreenViewModel.searchAlbumsResult.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    Column(Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = pagerState.currentPage) {
            listOf("Artists", "Albums", "Tracks").forEachIndexed { index, item ->
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
        HorizontalPager(count = 3, state = pagerState) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                when (it) {
                    0 -> itemsIndexed(searchArtistsResult.value, key = { index, it ->
                        it.id
                    }) { index, it ->
                        ArtistHorizontalPreview(
                            onClick = {
                                detailsViewModel.artistDetailScreenState.value =
                                    ArtistDetailScreenState(
                                        specificArtistFromSpotifyDTO = SpecificArtistFromSpotifyDTO(
                                            external_urls = it.external_urls,
                                            followers = it.followers,
                                            genres = it.genres,
                                            id = it.id,
                                            images = it.images,
                                            name = it.name,
                                            popularity = 0,
                                            type = it.type,
                                            uri = it.uri
                                        ),
                                        topTracks = TopTracksDTO(listOf()),
                                        albumSearchDTO = Albums(
                                            items = listOf(),
                                            limit = 0,
                                            offset = 0,
                                            total = 0
                                        )
                                    )
                                detailsViewModel.loadArtistInfo(it.id)
                                navController.navigate(NavigationRoutes.ARTIST_DETAILS.name)
                            },
                            artistImgUrl = if (it.images.isNotEmpty()) it.images.last().url else "",
                            artistName = it.name
                        )
                    }

                    1 -> itemsIndexed(searchAlbumsResult.value, key = { index, it ->
                        it.id
                    }) { index, it ->
                        AlbumxTrackHorizontalPreview(
                            onClick = {
                                detailsViewModel.albumScreenState = AlbumDetailScreenState(
                                    covertArtImgUrl = flowOf(),
                                    albumImgUrl = it.images.first().url,
                                    albumTitle = it.name,
                                    artists = it.artists.map { it.name },
                                    wikipediaExtractText = flowOf(),
                                    releaseDate = it.release_date,
                                    trackList = flowOf()
                                )
                                detailsViewModel.loadAlbumInfo(
                                    albumID = it.id,
                                    albumName = it.name,
                                    artistID = it.artists.map { it.id }.random()
                                )
                                navController.navigate(NavigationRoutes.ALBUM_DETAILS.name)
                            },
                            isExplicit = false,
                            itemType = it.album_type.capitalize(),
                            albumImgUrl = if (it.images.isNotEmpty()) it.images.last().url else "",
                            albumTitle = it.name,
                            artistName = it.artists.joinToString { it.name }
                        )
                    }

                    2 -> itemsIndexed(searchTracksResult.value, key = { index, it ->
                        it.id
                    }) { index, it ->
                        AlbumxTrackHorizontalPreview(
                            onClick = {
                                navController.navigate(NavigationRoutes.TRACK_DETAILS.name)
                            },
                            isExplicit = it.explicit,
                            itemType = it.type.capitalize(),
                            albumImgUrl = if (it.album.images.isNotEmpty()) it.album.images.last().url else "",
                            albumTitle = it.name,
                            artistName = it.artists.joinToString { it.name }
                        )
                    }
                }
            }
        }
    }
}