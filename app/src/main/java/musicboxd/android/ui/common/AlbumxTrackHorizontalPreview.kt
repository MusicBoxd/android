package musicboxd.android.ui.common

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
import androidx.compose.material.icons.filled.Explicit
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun AlbumxTrackHorizontalPreview(
    itemType: String,
    albumImgUrl: String,
    albumTitle: String,
    artistName: String,
    isExplicit: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoilImage(
            imgUrl = albumImgUrl,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentDescription = "$albumTitle Cover Art"
        )
        Spacer(modifier = Modifier.width(15.dp))
        Column {
            Text(text = albumTitle, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(5.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isExplicit) {
                    Icon(
                        imageVector = Icons.Default.Explicit,
                        contentDescription = "Explicit $itemType",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                }
                Text(
                    text = "$itemType • $artistName",
                    style = MaterialTheme.typography.titleSmall,
                    color = LocalContentColor.current.copy(0.9f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}