package musicboxd.android.ui.common

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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

@Composable
fun AlbumxTrackCover(albumxTrackCoverState: AlbumxTrackCoverState) {
    val colorScheme = MaterialTheme.colorScheme
    Log.d("10MinMail", albumxTrackCoverState.covertImgUrl)
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
                Text(
                    text = "${albumxTrackCoverState.itemType} • ${albumxTrackCoverState.itemArtists.joinToString { it }}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    softWrap = true
                )
            }
        }
    }
}