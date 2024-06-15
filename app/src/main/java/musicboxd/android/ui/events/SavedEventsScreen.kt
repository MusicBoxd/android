package musicboxd.android.ui.events

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import musicboxd.android.ui.common.CoilImage
import musicboxd.android.ui.common.fadedEdges

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedEventsScreen() {
    val localUriHandler = LocalUriHandler.current
    val savedEventsScreenVM: SavedEventsScreenVM = hiltViewModel()
    val savedEventsData = savedEventsScreenVM.savedEventsData.collectAsStateWithLifecycle()
    val topAppBarScrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(topAppBarScrollBehaviour.nestedScrollConnection), topBar = {
        MediumTopAppBar(scrollBehavior = topAppBarScrollBehaviour, title = {
            Text(
                text = "Saved Events",
                fontSize = 16.sp,
                style = MaterialTheme.typography.titleMedium
            )
        })
    }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            item {
                Spacer(modifier = Modifier.height(15.dp))
            }
            items(savedEventsData.value, key = {
                it.eventId
            }) {
                val detailsExpanded = rememberSaveable {
                    mutableStateOf(false)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .clickable {
                            detailsExpanded.value = !detailsExpanded.value
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .height(75.dp)
                            .width(75.dp)
                    ) {
                        CoilImage(
                            imgUrl = it.artistImg,
                            modifier = Modifier
                                .size(65.dp)
                                .clip(CircleShape)
                                .fadedEdges(MaterialTheme.colorScheme),
                            contentDescription = ""
                        )
                        Column(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(
                                    RoundedCornerShape(15.dp)
                                )
                                .background(CardDefaults.cardColors().containerColor)
                                .align(Alignment.BottomEnd),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = it.artistTourDTO.date.substringBefore(" "),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = it.artistTourDTO.date.substringAfter(" "),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(15.dp))
                    Column {
                        Text(
                            text = it.artistTourDTO.location,
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = it.artistTourDTO.venue,
                            style = MaterialTheme.typography.titleSmall,
                            color = LocalContentColor.current.copy(0.65f)
                        )
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        IconButton(onClick = {
                            detailsExpanded.value = !detailsExpanded.value
                        }) {
                            Icon(
                                imageVector = if (!detailsExpanded.value) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                                contentDescription = ""
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .animateContentSize()
                ) {
                    if (detailsExpanded.value) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = it.eventsDetailsDTO.eventTime,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 15.dp, top = 15.dp)
                            )
                            Divider(
                                modifier = Modifier.padding(15.dp),
                                color = MaterialTheme.colorScheme.outline.copy(0.25f)
                            )
                            Text(
                                text = "Tickets available via",
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 15.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        localUriHandler.openUri(it.eventsDetailsDTO.ticketsDetails.ticketBuyingUrl)
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CoilImage(
                                    imgUrl = it.eventsDetailsDTO.ticketsDetails.ticketSellingPlatformImgURL,
                                    modifier = Modifier
                                        .padding(15.dp)
                                        .size(45.dp)
                                        .clip(RoundedCornerShape(10.dp)),
                                    contentDescription = ""
                                )
                                Text(
                                    text = it.eventsDetailsDTO.ticketsDetails.ticketSellingPlatformName,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 15.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    IconButton(onClick = {
                                        localUriHandler.openUri(it.eventsDetailsDTO.ticketsDetails.ticketBuyingUrl)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowOutward,
                                            contentDescription = ""
                                        )
                                    }
                                }
                            }
                            Divider(
                                modifier = Modifier.padding(15.dp),
                                color = MaterialTheme.colorScheme.outline.copy(0.25f)
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                }

            }
        }
    }
}