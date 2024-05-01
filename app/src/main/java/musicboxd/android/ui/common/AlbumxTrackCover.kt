package musicboxd.android.ui.common

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AlbumxTrackCover() {
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height((200 + 50).dp)
    ) {
        CoilImage(
            imgUrl = "https://www.billboard.com/wp-content/uploads/media/kanye-west-2015-def-jam_inez-and-vinoodh-billboard-650.jpg?w=650",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .fadedBottomEdges(colorScheme),
            contentDescription = "null"
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(15.dp)
                .align(Alignment.BottomStart)
        ) {
            CoilImage(
                imgUrl = "https://upload.wikimedia.org/wikipedia/en/a/a3/Kanyewest_collegedropout.jpg",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentDescription = "null"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "The College Dropout",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    softWrap = true
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Album • Kanye West",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    softWrap = true
                )
            }
        }
    }
}