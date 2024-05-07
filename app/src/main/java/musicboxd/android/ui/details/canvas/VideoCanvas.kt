package musicboxd.android.ui.details.canvas

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import musicboxd.android.ui.common.CoilImage
import musicboxd.android.ui.details.DetailsViewModel

@OptIn(ExperimentalPagerApi::class)
@Composable
fun VideoCanvas(
    detailsViewModel: DetailsViewModel,
    navController: NavController
) {
    val localContext = LocalContext.current
    val isPlayerReady = rememberSaveable {
        mutableStateOf(false)
    }
    val pagerState = rememberPagerState()
    val systemUiController = rememberSystemUiController()
    systemUiController.setNavigationBarColor(MaterialTheme.colorScheme.surface)
    systemUiController.setStatusBarColor(MaterialTheme.colorScheme.surface)
    val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
    val mediaPlayer = remember {
        MediaPlayer().apply {
            setAudioAttributes(audioAttributes)
        }
    }
    mediaPlayer.setAudioAttributes(audioAttributes)
    val currentDuration = remember {
        mutableFloatStateOf(0f)
    }
    val albumScreenState = detailsViewModel.albumScreenState
    val trackList = detailsViewModel.albumScreenState.trackList.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )
    val currentPage = rememberSaveable {
        mutableIntStateOf(0)
    }
    val coroutineScope = rememberCoroutineScope()
    val canvasList = detailsViewModel.canvasUrl
    LaunchedEffect(key1 = pagerState.currentPage, key2 = trackList.value.size) {
        currentPage.intValue = pagerState.currentPage
        mediaPlayer.stop()
        mediaPlayer.reset()
        if (trackList.value.isEmpty()) {
            return@LaunchedEffect
        }
        mediaPlayer.setDataSource(trackList.value[pagerState.currentPage].preview_url)
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
    val showNonCanvasModeLayout = rememberSaveable {
        mutableStateOf(false)
    }
    VerticalPager(count = trackList.value.size, state = pagerState) {
        val exoPlayer = remember(canvasList.value) {
            ExoPlayer.Builder(localContext).build().apply {
                if (canvasList.value.isEmpty()) {
                    return@apply
                }
                setMediaItem(
                    MediaItem.fromUri(
                        try {
                            showNonCanvasModeLayout.value = false
                            canvasList.value[it]
                        } catch (_: Exception) {
                            showNonCanvasModeLayout.value = true
                            ""
                        }
                    )
                )
                repeatMode = ExoPlayer.REPEAT_MODE_ALL
                isPlayerReady.value = playWhenReady
                prepare()
                play()
            }
        }
        if (showNonCanvasModeLayout.value || (canvasList.value.size > it && canvasList.value[it].isEmpty() && canvasList.value.isNotEmpty())) {
            NonCanvasMode(
                previewCardColor = detailsViewModel.previewCardColor.value,
                imgUrl = albumScreenState.albumImgUrl,
                title = trackList.value[it].name,
                artists = albumScreenState.artists.map { it.name },
                albumName = albumScreenState.albumTitle,
                currentDuration = currentDuration,
                mediaPlayer = mediaPlayer,
                itemType = albumScreenState.itemType
            )
            return@VerticalPager
        }
        Box(Modifier.fillMaxSize()) {
            if (canvasList.value.isNotEmpty()) {
                DisposableEffect(
                    AndroidView(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                        factory = {
                            PlayerView(localContext).apply {
                                player = exoPlayer
                                useController = false
                                this.setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                            }
                        })
                ) {
                    onDispose {
                        exoPlayer.release()
                    }
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
                        imgUrl = albumScreenState.albumImgUrl,
                        modifier = Modifier
                            .size(75.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentDescription = "null"
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = trackList.value[it].name,
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "${albumScreenState.albumTitle} • ${albumScreenState.itemType} • ${albumScreenState.artists.joinToString { it.name }}",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (currentPage.intValue == it) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        /* Slider(
                             thumb = {},
                             value = currentDuration.floatValue,
                             onValueChange = {
                                 currentDuration.floatValue = it
                                 mediaPlayer.seekTo((it * mediaPlayer.duration).toInt())
                             }, modifier = Modifier
                                 .fillMaxWidth(0.5f)
                                 .align(Alignment.CenterStart)
                         )*/
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
                                Icon(
                                    imageVector = Icons.Default.MoreVert, contentDescription = null
                                )
                            }
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.height(22.dp))
                }
            }
        }
    }
    BackHandler {
        coroutineScope.launch {
            awaitAll(async {
                mediaPlayer.stop()
                mediaPlayer.reset()
            }, async {
                navController.popBackStack()
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NonCanvasMode(
    previewCardColor: Color,
    imgUrl: String,
    title: String,
    artists: List<String>,
    albumName: String,
    currentDuration: MutableFloatState,
    mediaPlayer: MediaPlayer,
    itemType: String
) {
    val infiniteTransition = rememberInfiniteTransition(label = "VideoCanvas")
    val animatedProgress = infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "VideoCanvas Gradient"
    )
    val colorScheme = MaterialTheme.colorScheme
    val gradientColors = listOf(
        colorScheme.surface,
        previewCardColor
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .drawWithCache {
                    val height = size.height
                    val offset = height * animatedProgress.value
                    val brush = Brush.linearGradient(
                        colors = gradientColors,
                        start = Offset(0f, 5f),
                        end = Offset(offset, height)
                    )
                    onDrawBehind {
                        drawRect(
                            brush = brush,
                            blendMode = BlendMode.SrcIn
                        )
                    }
                },
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(), contentAlignment = Alignment.CenterStart
            ) {
                CoilImage(
                    imgUrl = imgUrl,
                    modifier = Modifier
                        .size(225.dp)
                        .padding(start = 15.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentDescription = "Album Cover Art"
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 20.sp,
                color = contentColorFor(backgroundColor = colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append(albumName)
                    }
                    append(" • $itemType • ${artists.joinToString { it }}")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp),
                style = MaterialTheme.typography.titleSmall,
                color = contentColorFor(backgroundColor = colorScheme.surface),
                textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.height(15.dp))
            Box(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 15.dp)
                    .navigationBarsPadding(),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    /*Slider(
                        colors = SliderDefaults.colors(inactiveTrackColor = Color.Transparent),
                        thumb = {},
                        value = currentDuration.floatValue,
                        onValueChange = {
                            currentDuration.floatValue = it
                            mediaPlayer.seekTo((it * mediaPlayer.duration).toInt())
                        }, modifier = Modifier
                            .fillMaxWidth(0.5f)
                    )*/
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = null
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.BookmarkBorder,
                                contentDescription = null
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert, contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}