package musicboxd.android.ui.user.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import musicboxd.android.ui.common.AlbumxTrackHorizontalPreview
import musicboxd.android.ui.common.CoilImage
import musicboxd.android.ui.common.fadedEdges
import musicboxd.android.ui.navigation.NavigationRoutes
import musicboxd.android.ui.theme.MusicBoxdTheme
import musicboxd.android.utils.Spacing

@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun UserProfile(navController: NavController) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    MusicBoxdTheme {
        BoxWithConstraints {
            val screenHeight = maxHeight
            val screenWidth = maxWidth
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Column {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(165.dp)
                    ) {
                        CoilImage(
                            imgUrl = "https://image-cdn.hypb.st/https%3A%2F%2Fhypebeast.com%2Fimage%2F2018%2F08%2Fkanye-west-saint-pablo-tour-features-floating-stage-001.jpg?cbr=1&q=90",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .fadedEdges(MaterialTheme.colorScheme),
                            contentDescription = "",
                            alignment = Alignment.Center
                        )
                        Row(
                            modifier = Modifier.align(Alignment.BottomStart),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CoilImage(
                                imgUrl = "https://yt3.googleusercontent.com/ytc/AIdro_nG4kBTH_JqCA5PUmjKbJZBTj3M6TsAJ0lfd-k3liFnVcE=s900-c-k-c0x00ffffff-no-rj",
                                modifier = Modifier
                                    .padding(start = 15.dp)
                                    .size(85.dp)
                                    .clip(CircleShape)
                                    .border(
                                        BorderStroke(2.5.dp, LocalContentColor.current), CircleShape
                                    ),
                                contentDescription = "",
                                alignment = Alignment.TopCenter
                            )
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "0",
                                            fontSize = 20.sp,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Reviews",
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "191",
                                            fontSize = 20.sp,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Followers",
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "994",
                                            fontSize = 20.sp,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Following",
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                    }
                                }
                            }
                        }
                        FilledTonalIconButton(
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = IconButtonDefaults.filledTonalIconButtonColors().containerColor.copy(
                                    0.75f
                                )
                            ), onClick = { /*TODO*/ }, modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(5.dp)
                        ) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "")
                        }
                        FilledTonalIconButton(
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = IconButtonDefaults.filledTonalIconButtonColors().containerColor.copy(
                                    0.75f
                                )
                            ), onClick = { /*TODO*/ }, modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(5.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.width(screenWidth - 130.dp)) {
                            Text(
                                text = "Nasty Nas",
                                fontSize = 18.sp,
                                modifier = Modifier.padding(start = 15.dp, top = 15.dp),
                                style = MaterialTheme.typography.titleLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "@nas",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(start = 15.dp),
                                style = MaterialTheme.typography.titleSmall,
                                color = LocalContentColor.current.copy(0.75f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 10.dp)
                        ) {
                            FilledTonalIconButton(onClick = {
                                navController.navigate(NavigationRoutes.EDIT_PROFILE.name)
                            }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "")
                            }
                            Text(
                                text = "•",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                            )
                            FilledTonalIconButton(onClick = { /*TODO*/ }) {
                                Icon(imageVector = Icons.Default.Settings, contentDescription = "")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "I thought Jordans and a gold chain was living it up.",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 15.dp, end = 10.dp),
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 4
                    )
                    Row(
                        modifier = Modifier.padding(start = 15.dp, top = 15.dp, end = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "",
                            modifier = Modifier.size(20.dp),
                            tint = LocalContentColor.current.copy(0.75f)
                        )
                        Text(
                            text = "Queensbridge, boy! Once again, boy!",
                            modifier = Modifier.padding(start = 5.dp, end = 15.dp),
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1,
                            color = LocalContentColor.current.copy(0.75f)
                        )
                    }
                    Row(
                        modifier = Modifier.padding(start = 15.dp, top = 10.dp, end = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "",
                            modifier = Modifier.size(20.dp),
                            tint = LocalContentColor.current.copy(0.75f)
                        )
                        Text(
                            text = "Joined 19 April 1994",
                            modifier = Modifier.padding(start = 5.dp, end = 15.dp),
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1,
                            color = LocalContentColor.current.copy(0.75f)
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight)
                ) {
                    ScrollableTabRow(
                        modifier = Modifier.fillMaxWidth(),
                        selectedTabIndex = pagerState.currentPage
                    ) {
                        listOf(
                            "Reviews", "Recommendations", "Listened", "Lists", "Liked"
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
                    HorizontalPager(
                        count = 4,
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxHeight()
                            .nestedScroll(remember {
                                object : NestedScrollConnection {
                                    override fun onPreScroll(
                                        available: Offset, source: NestedScrollSource
                                    ): Offset {
                                        return if (available.y > 0) Offset.Zero else Offset(
                                            x = 0f, y = -scrollState.dispatchRawDelta(-available.y)
                                        )
                                    }
                                }
                            })
                    ) {
                        LazyColumn {
                            items(25) {
                                AlbumxTrackHorizontalPreview(
                                    onClick = { /*TODO*/ },
                                    itemType = listOf("Album", "Track").random(),
                                    albumImgUrl = listOf(
                                        "https://media.gq-magazine.co.uk/photos/62bb1ff22176d0b26a09d6c7/master/w_1600,c_limit/MMLP1_alternate_cover.jpg",
                                        "https://media.gq-magazine.co.uk/photos/62bb1f2d916a16217c0cec49/master/w_1600,c_limit/c2dbe79ace8a4998c9955214bf6ee345.jpg",
                                        "https://media.gq-magazine.co.uk/photos/5eb94fa161ee223ac16caad2/master/w_1600,c_limit/20200511-album-14.jpg",
                                        "https://media.gq-magazine.co.uk/photos/5eb94fa31578fa0ec3478b81/master/w_1600,c_limit/20200511-album-04.jpg",
                                        "https://media.gq-magazine.co.uk/photos/61f007542bcbf0978c2b7b2d/master/w_1600,c_limit/250122_hiphop_04.jpg",
                                        "https://media.gq-magazine.co.uk/photos/61f007543d7efe70a7943d28/master/w_1600,c_limit/250122_hiphop_01.jpg"
                                    ).random(),
                                    albumTitle = listOf(
                                        "Yeezus",
                                        "Illmatic",
                                        "GKMC",
                                        "It Was Written",
                                        "DAMN",
                                        "Dr Dre"
                                    ).random(),
                                    artistName = listOf(
                                        "2Pac", "Kanye West", "Nas", "Kendrick Lamar"
                                    ).random(),
                                    isExplicit = true
                                )
                            }
                            item {
                                Spacer(modifier = Modifier.height(Spacing.BOTTOM_NAV_BAR_SPACING))
                            }
                        }
                    }
                }
            }
        }
    }
}