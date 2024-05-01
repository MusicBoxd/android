package musicboxd.android.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun ArtistHorizontalPreview(onClick: () -> Unit, artistImgUrl: String, artistName: String) {
    Row(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoilImage(
            imgUrl = artistImgUrl, modifier = Modifier
                .size(50.dp)
                .clip(CircleShape), contentDescription = "Artist Image"
        )
        Spacer(modifier = Modifier.width(15.dp))
        Column {
            Text(text = artistName, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Artist",
                style = MaterialTheme.typography.titleSmall,
                color = LocalContentColor.current.copy(0.9f),
            )
        }
    }
}