package musicboxd.android.ui.details.canvas

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.coroutines.delay
import musicboxd.android.ui.common.CoilImage

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VideoCanvas() {
    val localContext = LocalContext.current
    val isPlayerReady = rememberSaveable {
        mutableStateOf(false)
    }
    val systemUiController = rememberSystemUiController()
    systemUiController.setNavigationBarColor(MaterialTheme.colorScheme.surface)
    systemUiController.setStatusBarColor(MaterialTheme.colorScheme.surface)
    val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()
    val mediaPlayer = remember {
        MediaPlayer().apply {
            setAudioAttributes(audioAttributes)
        }
    }
    mediaPlayer.setAudioAttributes(audioAttributes)
    val currentDuration = remember {
        mutableFloatStateOf(0f)
    }
    LaunchedEffect(key1 = mediaPlayer.isPlaying) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.reset()
        }
        mediaPlayer.setDataSource("https://small.fileditchstuff.me/s10/NKWjNZiXLQKULYDWTAVg.mp3")
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            it.start()
        }
        mediaPlayer.isLooping = true
        while (true) {
            currentDuration.floatValue =
                mediaPlayer.currentPosition.toFloat() / mediaPlayer.duration.toFloat()
            delay(500L)
        }
    }
    VerticalPager(count = 5) {
        val canvasList = listOf(
            "https://canvaz.scdn.co/upload/licensor/7JGwF0zhX9oItt9901OvB5/video/8ca86e263dbb43c595c688dc2ffd1047.cnvs.mp4",
            "https://canvaz.scdn.co/upload/licensor/7JGwF0zhX9oItt9901OvB5/video/79e3db8eac694f6ab712462fb441689f.cnvs.mp4",
            "https://canvaz.scdn.co/upload/licensor/7JGwF0zhX9oItt9901OvB5/video/b05e5511a78c4fc3a2247e433f1f744b.cnvs.mp4"
        )
        val exoPlayer = remember {
            ExoPlayer.Builder(localContext).build().apply {
                setMediaItem(MediaItem.fromUri(canvasList.random()))
                repeatMode = ExoPlayer.REPEAT_MODE_ALL
                isPlayerReady.value = playWhenReady
                prepare()
                play()
            }
        }
        Box(Modifier.fillMaxSize()) {
            DisposableEffect(
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    factory = {
                        PlayerView(localContext).apply {
                            player = exoPlayer
                            useController = false
                            this.setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                        }
                    }
                )
            ) {
                onDispose {
                    exoPlayer.release()
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.surface,
                            )
                        )
                    )
                    .padding(top = 50.dp, start = 15.dp, end = 15.dp)
                    .align(Alignment.BottomStart)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CoilImage(
                        imgUrl = "https://i.scdn.co/image/ab67616d00001e0226f7f19c7f0381e56156c94a",
                        modifier = Modifier
                            .size(75.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentDescription = "null"
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Through The Wire",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "Single • Kanye West",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Slider(
                        thumb = {},
                        value = currentDuration.floatValue,
                        onValueChange = {
                            currentDuration.floatValue = it
                            mediaPlayer.seekTo((it * mediaPlayer.duration).toInt())
                        }, modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .align(Alignment.CenterStart)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        FilledTonalIconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = null
                            )
                        }
                        FilledTonalIconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.BookmarkBorder,
                                contentDescription = null
                            )
                        }
                        FilledTonalIconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                        }
                    }
                }
            }
        }
    }
}