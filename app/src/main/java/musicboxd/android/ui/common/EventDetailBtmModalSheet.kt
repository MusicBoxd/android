package musicboxd.android.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import musicboxd.android.data.remote.scrape.artist.tour.model.ArtistTourDTO
import musicboxd.android.ui.details.DetailsViewModel
import musicboxd.android.ui.notifications.common.OnTourInfoComposable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailBtmModalSheet(
    isVisible: MutableState<Boolean>,
    eventID: MutableState<String>,
    detailsViewModel: DetailsViewModel,
    artistTourDTO: ArtistTourDTO
) {
    val coroutineScope = rememberCoroutineScope()
    val bottomModalSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val eventData = detailsViewModel.specificConcertDetails.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = isVisible.value, eventID.value) {
        if (isVisible.value) {
            bottomModalSheetState.expand()
        }
    }
    val localUriHandler = LocalUriHandler.current
    if (isVisible.value) {
        ModalBottomSheet(onDismissRequest = {
            coroutineScope.launch {
                bottomModalSheetState.hide()
            }.invokeOnCompletion {
                isVisible.value = false
            }
        }) {
            if (eventData.value.eventTime.isEmpty()) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(50.dp)
                )
                return@ModalBottomSheet
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                OnTourInfoComposable(
                    artistTourDTO, modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp)
                )
                Text(
                    text = eventData.value.eventTime,
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
                            localUriHandler.openUri(eventData.value.ticketsDetails.ticketBuyingUrl)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CoilImage(
                        imgUrl = eventData.value.ticketsDetails.ticketSellingPlatformImgURL,
                        modifier = Modifier
                            .padding(15.dp)
                            .size(45.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentDescription = ""
                    )
                    Text(
                        text = eventData.value.ticketsDetails.ticketSellingPlatformName,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 15.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        IconButton(onClick = {
                            localUriHandler.openUri(eventData.value.ticketsDetails.ticketBuyingUrl)
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowOutward,
                                contentDescription = ""
                            )
                        }
                    }
                }
                FilledTonalButton(
                    onClick = { /*TODO*/ }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp)
                ) {
                    Text(
                        text = "Save this event",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(15.dp)
                        .navigationBarsPadding()
                )
            }
        }
    }
}