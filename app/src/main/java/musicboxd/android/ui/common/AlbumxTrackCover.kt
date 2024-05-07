package musicboxd.android.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import musicboxd.android.ui.details.DetailsViewModel
import musicboxd.android.ui.navigation.NavigationRoutes

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AlbumxTrackCover(
    albumxTrackCoverState: AlbumxTrackCoverState,
    detailsViewModel: DetailsViewModel,
    navController: NavController
) {
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height((200 + 50).dp)
    ) {
        CoilImage(
            alignment = Alignment.TopEnd,
            imgUrl = rememberSaveable(albumxTrackCoverState.covertImgUrl) {
                mutableStateOf(albumxTrackCoverState.covertImgUrl)
            }.value,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .fadedEdges(colorScheme),
            contentDescription = "null"
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(15.dp)
                .align(Alignment.BottomStart)
        ) {
            CoilImage(
                imgUrl = albumxTrackCoverState.mainImgUrl,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentDescription = "null"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = albumxTrackCoverState.itemTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    softWrap = true,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(5.dp))
                FlowRow(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "${albumxTrackCoverState.itemType} • ",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        softWrap = true,
                        maxLines = 1
                    )
                    albumxTrackCoverState.itemArtists.forEachIndexed { index, it ->
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.clickable {
                                detailsViewModel.loadArtistInfo(
                                    detailsViewModel.albumScreenState.artists[index].id,
                                    detailsViewModel.albumScreenState.artists[index].name,
                                    navigatingFromAlbumScreen = true
                                )
                                navController.navigate(NavigationRoutes.ARTIST_DETAILS.name)
                            }
                        )
                        if (albumxTrackCoverState.itemArtists.last() != it)
                            Text(
                                text = ", ",
                                style = MaterialTheme.typography.titleSmall
                            )
                    }
                }
            }
        }
    }
}