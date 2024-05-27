package musicboxd.android.ui.notifications.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import musicboxd.android.ui.common.CoilImage

@Composable
fun FromArtistComposable() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CoilImage(
            imgUrl = "https://static.wikia.nocookie.net/kanyewest/images/a/af/Nas.jpg/revision/latest?cb=20240125202947",
            modifier = Modifier
                .size(65.dp)
                .clip(CircleShape),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.width(15.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "From",
                style = MaterialTheme.typography.titleSmall,
                color = LocalContentColor.current.copy(0.75f)
            )
            Text(
                text = "Nas",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 20.sp
            )
        }
    }
}