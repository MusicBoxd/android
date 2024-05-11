package musicboxd.android.ui.search.charts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import musicboxd.android.ui.common.CoilImage
import musicboxd.android.ui.common.fadedEdges

@Composable
fun ChartCard(text: String, imgURL: String, index: Int, onClick: () -> Unit = {}) {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .padding(
                start = if (index == 0 || index % 2 == 0) 15.dp else 7.5.dp,
                end = if (index % 2 != 0 || index != 0) 15.dp else 7.5.dp,
                top = 7.5.dp,
                bottom = 7.5.dp
            )
            .clip(CardDefaults.shape)
            .clickable { onClick() }
    ) {
        Box {
            CoilImage(
                imgUrl = imgURL,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .fadedEdges(colorScheme),
                contentDescription = ""
            )
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(
                        Alignment.BottomStart
                    )
                    .padding(10.dp)
            )
        }
    }
}