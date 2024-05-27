package musicboxd.android.ui.notifications.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import musicboxd.android.ui.common.CoilImage

@Composable
fun NewReleaseComposable() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp, start = 45.dp, end = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoilImage(
            imgUrl = "https://upload.wikimedia.org/wikipedia/en/5/5f/Magic_3_by_Nas.jpg",
            modifier = Modifier
                .size(55.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = "Magic 3", style = MaterialTheme.typography.titleLarge,
                maxLines = 1, overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp
            )
            Text(
                text = "Album",
                style = MaterialTheme.typography.titleSmall,
                color = LocalContentColor.current.copy(0.65F)
            )
        }
    }
}