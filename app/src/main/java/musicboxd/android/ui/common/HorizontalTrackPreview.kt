package musicboxd.android.ui.common

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Explicit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalTrackPreview(
    trackNumber: String,
    trackName: String,
    isExplicit: Boolean,
    artists: List<String>,
    currentTrackID: String,
    selectedTrackId: String,
    isAnyTrackIsPlayingState: MutableState<Boolean>,
    isAnyTrackInLoadingState: MutableState<Boolean>,
    onPlayClick: () -> Unit,
    currentPlayingTrackDurationAsFloat: MutableFloatState,
    trackImgUrl: String = ""
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 10.dp, top = 7.5.dp, bottom = 7.5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            if (trackImgUrl.isEmpty()) {
                Spacer(modifier = Modifier.width(25.dp))
                Text(
                    modifier = Modifier.width(25.dp),
                    color = LocalContentColor.current.copy(0.9f),
                    text = trackNumber,
                    style = MaterialTheme.typography.titleSmall
                )
            } else {
                Spacer(modifier = Modifier.width(10.dp))
                CoilImage(
                    imgUrl = trackImgUrl,
                    modifier = Modifier
                        .size(55.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    contentDescription = "$trackName cover art"
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = trackName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    if (isExplicit) {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            imageVector = Icons.Default.Explicit,
                            contentDescription = "Explicit Track Icon"
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                    }
                    Text(
                        text = artists.joinToString { it },
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        textAlign = TextAlign.Start,
                        color = LocalContentColor.current.copy(0.9f),
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.Center) {
                if (selectedTrackId == currentTrackID && (isAnyTrackIsPlayingState.value || isAnyTrackInLoadingState.value)) {
                    if (isAnyTrackIsPlayingState.value) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp),
                            strokeWidth = 2.5.dp,
                            progress = currentPlayingTrackDurationAsFloat.floatValue
                        )
                    } else {
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp),
                            strokeWidth = 2.5.dp
                        )
                    }
                }
                IconButton(onClick = {
                    onPlayClick()
                }) {
                    Icon(
                        imageVector = if (isAnyTrackInLoadingState.value && selectedTrackId == currentTrackID) Icons.Default.Audiotrack else if (isAnyTrackIsPlayingState.value && selectedTrackId == currentTrackID) Icons.Default.Stop else Icons.Default.PlayArrow,
                        contentDescription = null
                    )
                }
            }
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
            }
        }
    }
}