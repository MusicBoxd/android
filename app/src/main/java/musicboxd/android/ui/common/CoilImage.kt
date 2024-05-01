package musicboxd.android.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun CoilImage(
    alignment: Alignment = Alignment.Center,
    imgUrl: String,
    modifier: Modifier,
    contentDescription: String,
    contentScale: ContentScale = ContentScale.Crop
) {
    AsyncImage(
        alignment = alignment,
        model = ImageRequest.Builder(LocalContext.current).data(imgUrl).crossfade(true)
            .build(), contentDescription = contentDescription,
        modifier = modifier, contentScale = contentScale
    )
}