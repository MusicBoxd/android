package musicboxd.android.ui.details.album

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Reviews
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import musicboxd.android.R
import musicboxd.android.ui.common.AlbumxTrackCover
import musicboxd.android.ui.common.AlbumxTrackCoverState
import musicboxd.android.ui.common.fadedBottomEdges
import musicboxd.android.ui.details.DetailsViewModel
import musicboxd.android.ui.theme.MusicBoxdTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AlbumDetailScreen(
    albumDetailScreenState: AlbumDetailScreenState,
    detailsViewModel: DetailsViewModel
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    val isBtmSheetVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val localContext = LocalContext.current
    val wikipediaExtractText =
        albumDetailScreenState.wikipediaExtractText.collectAsStateWithLifecycle("")
    val trackList =
        albumDetailScreenState.trackList.collectAsStateWithLifecycle(initialValue = emptyList())
    val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()
    val mediaPlayer = remember {
        MediaPlayer().apply {
            setAudioAttributes(audioAttributes)
        }
    }
    val isAnyTrackIsPlayingState = rememberSaveable {
        mutableStateOf(false)
    }
    val isAnyTrackInLoadingState = rememberSaveable {
        mutableStateOf(false)
    }
    val selectedTrackId = rememberSaveable {
        mutableStateOf("")
    }
    val currentPlayingTrackDurationAsFloat = rememberSaveable {
        mutableFloatStateOf(0f)
    }
    LaunchedEffect(key1 = isAnyTrackIsPlayingState.value) {
        while (isAnyTrackIsPlayingState.value) {
            currentPlayingTrackDurationAsFloat.floatValue =
                mediaPlayer.currentPosition.toFloat() / 30000f
            delay(500L)
        }
    }
    MusicBoxdTheme {
        val colorScheme = MaterialTheme.colorScheme
        LazyColumn(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            item {
                AlbumxTrackCover(
                    albumxTrackCoverState = AlbumxTrackCoverState(
                        covertImgUrl = albumDetailScreenState.covertArtImgUrl.collectAsStateWithLifecycle(
                            ""
                        ).value,
                        mainImgUrl = albumDetailScreenState.albumImgUrl,
                        itemTitle = albumDetailScreenState.albumTitle,
                        itemArtists = albumDetailScreenState.artists
                    )
                )
            }
            if (wikipediaExtractText.value.contains("album") && wikipediaExtractText.value.lowercase()
                    .contains(albumDetailScreenState.artists.random().lowercase())
            ) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.wikipedia_logo),
                            contentDescription = "Wikipedia Logo",
                            modifier = Modifier
                                .padding(start = 15.dp)
                                .size(20.dp)
                        )
                        Text(
                            text = " • Overview",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
                item {
                    Text(text = wikipediaExtractText.value,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .clickable {
                                isBtmSheetVisible.value = true
                                coroutineScope.launch {
                                    modalBottomSheetState.show()
                                }
                            }
                            .padding(15.dp)
                            .fadedBottomEdges(colorScheme),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis)
                }
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 15.dp, end = 15.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Default.Event,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Text(text = "• Released on ${albumDetailScreenState.releaseDate}",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .clickable {
                                isBtmSheetVisible.value = true
                                coroutineScope.launch {
                                    modalBottomSheetState.show()
                                }
                            }
                            .padding(start = 5.dp),
                        color = MaterialTheme.colorScheme.onSurface)
                }
            }
            item {
                Divider(
                    modifier = Modifier.padding(start = 15.dp, top = 15.dp, end = 15.dp),
                    color = MaterialTheme.colorScheme.outline.copy(0.25f)
                )
                Text(
                    text = "Your Activity",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(15.dp)
                )
            }
            item {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                    Row(
                        horizontalArrangement = Arrangement.Absolute.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(5) {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    modifier = Modifier.size(38.dp),
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null
                                )
                            }
                        }
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                    append("5.0")
                                }
                                append("/5.0")
                            },
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .align(Alignment.Bottom)
                                .padding(bottom = 7.dp)
                        )
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            text = "Listened",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            text = "Liked",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, top = 15.dp, end = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FilledTonalButton(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                    ) {
                        Icon(imageVector = Icons.Default.QueueMusic, contentDescription = null)
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Add to Listen Later",
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                    FilledTonalIconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                    }
                }
            }
            item {
                FilledTonalButton(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, bottom = 15.dp, top = 5.dp)
                ) {
                    Icon(imageVector = Icons.Default.RateReview, contentDescription = null)
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Add a Review",
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }
            item {
                Divider(
                    modifier = Modifier.padding(start = 15.dp, end = 15.dp),
                    color = MaterialTheme.colorScheme.outline.copy(0.25f)
                )
                Text(
                    text = "Community Activity",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(15.dp)
                )
            }
            item {
                Row(Modifier.fillMaxWidth()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(start = 15.dp, end = 7.5.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                modifier = Modifier.padding(15.dp),
                                imageVector = Icons.Default.Reviews,
                                contentDescription = null
                            )
                            Column {
                                Text(
                                    text = "Reviews",
                                    style = MaterialTheme.typography.titleMedium,
                                )
                                Text(
                                    text = "100",
                                    style = MaterialTheme.typography.titleSmall,
                                )
                            }
                        }
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 7.5.dp, end = 15.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                modifier = Modifier.padding(15.dp),
                                imageVector = Icons.Default.Group,
                                contentDescription = null
                            )
                            Column {
                                Text(
                                    text = "Lists",
                                    style = MaterialTheme.typography.titleMedium,
                                )
                                Text(
                                    text = "100",
                                    style = MaterialTheme.typography.titleSmall,
                                )
                            }
                        }
                    }
                }
            }
            item {
                Divider(
                    modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 15.dp),
                    color = MaterialTheme.colorScheme.outline.copy(0.25f)
                )
                if (trackList.value.isNotEmpty()) {
                    Text(
                        text = "Track list",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 15.dp, top = 15.dp, bottom = 7.5.dp)
                    )
                }
            }
            itemsIndexed(trackList.value) { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp, top = 7.5.dp, bottom = 7.5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(0.7f)
                    ) {
                        Spacer(modifier = Modifier.width(25.dp))
                        Text(
                            modifier = Modifier.width(25.dp),
                            color = LocalContentColor.current.copy(0.9f),
                            text = item.track_number.toString(),
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Start
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = item.artists.joinToString { it.name },
                                style = MaterialTheme.typography.titleSmall,
                                maxLines = 1,
                                textAlign = TextAlign.Start,
                                color = LocalContentColor.current.copy(0.9f),
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(contentAlignment = Alignment.Center) {
                            if (selectedTrackId.value == item.id && (isAnyTrackIsPlayingState.value || isAnyTrackInLoadingState.value)) {
                                if (isAnyTrackIsPlayingState.value) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(30.dp),
                                        strokeWidth = 2.5.dp,
                                        progress = currentPlayingTrackDurationAsFloat.floatValue
                                    )
                                } else {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(30.dp),
                                        strokeWidth = 2.5.dp
                                    )
                                }
                            }
                            IconButton(onClick = {
                                if (isAnyTrackIsPlayingState.value || isAnyTrackInLoadingState.value) {
                                    isAnyTrackInLoadingState.value = false
                                    isAnyTrackIsPlayingState.value = false
                                    mediaPlayer.stop()
                                    mediaPlayer.reset()
                                    return@IconButton
                                }
                                Toast
                                    .makeText(localContext, "Fetching Audio", Toast.LENGTH_SHORT)
                                    .show()
                                selectedTrackId.value = item.id
                                isAnyTrackInLoadingState.value = true
                                mediaPlayer.stop()
                                mediaPlayer.reset()
                                mediaPlayer.setDataSource(item.preview_url)
                                mediaPlayer.prepareAsync()
                                mediaPlayer.setOnPreparedListener {
                                    it.start()
                                    isAnyTrackIsPlayingState.value = true
                                }
                                mediaPlayer.setOnCompletionListener {
                                    it.stop()
                                    it.reset()
                                    isAnyTrackIsPlayingState.value = false
                                    isAnyTrackInLoadingState.value = false
                                }
                            }) {
                                Icon(
                                    imageVector = if (isAnyTrackIsPlayingState.value && selectedTrackId.value == item.id) Icons.Default.Stop else Icons.Default.PlayArrow,
                                    contentDescription = null
                                )
                            }
                        }
                        IconButton(onClick = { }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                        }
                    }
                }
            }
        }
        if (isBtmSheetVisible.value) {
            ModalBottomSheet(dragHandle = {}, onDismissRequest = {
                coroutineScope.launch {
                    modalBottomSheetState.hide()
                }.invokeOnCompletion {
                    isBtmSheetVisible.value = false
                }
            }) {
                AlbumxTrackCover(
                    albumxTrackCoverState = AlbumxTrackCoverState(
                        covertImgUrl = albumDetailScreenState.covertArtImgUrl.collectAsState(initial = "").value,
                        mainImgUrl = albumDetailScreenState.albumImgUrl,
                        itemTitle = albumDetailScreenState.albumTitle,
                        itemArtists = albumDetailScreenState.artists
                    )
                )
                Text(
                    text = albumDetailScreenState.wikipediaExtractText.collectAsState(initial = "").value,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(start = 15.dp, end = 15.dp)
                        .navigationBarsPadding(),
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.navigationBarsPadding()
                ) {
                    Icon(
                        modifier = Modifier.padding(start = 15.dp, end = 5.dp),
                        imageVector = Icons.Default.Info,
                        contentDescription = "Source Info"
                    )
                    Text(
                        text = "Info straight outta Wikipedia",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}