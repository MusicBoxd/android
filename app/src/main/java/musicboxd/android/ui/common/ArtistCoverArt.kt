package musicboxd.android.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ArtistCoverArt(artistCoverArtState: ArtistCoverArtState) {
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        CoilImage(
            imgUrl = artistCoverArtState.artistImgUrl,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .fadedEdges(colorScheme),
            contentDescription = "${artistCoverArtState.artistName} image"
        )
        Text(
            text = artistCoverArtState.artistName,
            style = MaterialTheme.typography.titleLarge,
            color = LocalContentColor.current,
            fontSize = 40.sp,
            maxLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(10.dp),
            softWrap = true
        )
    }
}