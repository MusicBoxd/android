package musicboxd.android.ui.notifications.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import musicboxd.android.data.remote.scrape.artist.tour.model.ArtistTourDTO

@Composable
fun OnTourInfoComposable(artistTourDTO: ArtistTourDTO, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .size(50.dp)
                .clip(
                    RoundedCornerShape(15.dp)
                )
                .background(CardDefaults.cardColors().containerColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = artistTourDTO.date.substringBefore(" "),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = artistTourDTO.date.substringAfter(" "),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = artistTourDTO.location, style = MaterialTheme.typography.titleLarge)
            Text(
                text = artistTourDTO.venue,
                style = MaterialTheme.typography.titleSmall,
                color = LocalContentColor.current.copy(0.65f)
            )
        }
    }
}