package musicboxd.android.ui.details.album

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicVideo
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Reviews
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import musicboxd.android.R
import musicboxd.android.ui.common.AlbumxTrackCover
import musicboxd.android.ui.common.AlbumxTrackCoverState
import musicboxd.android.ui.common.CoilImage
import musicboxd.android.ui.common.HorizontalTrackPreview
import musicboxd.android.ui.common.fadedEdges
import musicboxd.android.ui.details.DetailsViewModel
import musicboxd.android.ui.navigation.NavigationRoutes
import musicboxd.android.ui.theme.MusicBoxdTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AlbumDetailScreen(
    albumDetailScreenState: AlbumDetailScreenState,
    detailsViewModel: DetailsViewModel,
    navController: NavController
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    val isBtmSheetVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val localContext = LocalContext.current
    val wikipediaExtractText =
        albumDetailScreenState.albumWiki.collectAsStateWithLifecycle("")
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
    val localUriHandler = LocalUriHandler.current
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
                        itemArtists = albumDetailScreenState.artists,
                        itemType = albumDetailScreenState.itemType
                    ),
                    detailsViewModel = detailsViewModel,
                    navController = navController,
                )
            }
            if (wikipediaExtractText.value.contains("album") && wikipediaExtractText.value.lowercase()
                    .contains(albumDetailScreenState.artists.random().name.lowercase())
            ) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.lastfm),
                            contentDescription = "LastFM Logo",
                            modifier = Modifier
                                .padding(start = 15.dp)
                                .clip(CircleShape)
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
                            .padding(15.dp)
                            .clickable {
                                isBtmSheetVisible.value = true
                                coroutineScope.launch {
                                    modalBottomSheetState.show()
                                }
                            }
                            .fadedEdges(colorScheme),
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
                    Text(text = rememberSaveable(albumDetailScreenState.releaseDate) {
                        "• Released on ${albumDetailScreenState.releaseDate}"
                    },
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
            if (detailsViewModel.albumExternalLinks.value.isNotEmpty()) {
                item {
                    Divider(
                        modifier = Modifier.padding(start = 15.dp, top = 15.dp, end = 15.dp),
                        color = MaterialTheme.colorScheme.outline.copy(0.25f)
                    )
                    Text(
                        text = "Listen On",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(15.dp)
                    )
                }
                item {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 5.dp)
                    ) {
                        detailsViewModel.albumExternalLinks.value.forEach {
                            if (it.externalLink != "") {
                                IconButton(onClick = {
                                    localUriHandler.openUri(it.externalLink)
                                }) {
                                    CoilImage(
                                        imgUrl = it.imgURL, modifier = Modifier
                                            .size(24.dp)
                                            .clip(
                                                CircleShape
                                            ), contentDescription = ""
                                    )
                                }
                            }
                        }
                    }
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
                    onClick = {
                        navController.navigate(NavigationRoutes.CREATE_A_NEW_REVIEW.name)
                    },
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
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = detailsViewModel.previewCardColor.value
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                mediaPlayer.stop()
                                mediaPlayer.reset()
                                detailsViewModel.albumScreenState = albumDetailScreenState
                                navController.navigate(NavigationRoutes.VIDEO_CANVAS.name)
                            }
                    ) {
                        CoilImage(
                            imgUrl = rememberSaveable(albumDetailScreenState.albumImgUrl) {
                                mutableStateOf(albumDetailScreenState.albumImgUrl)
                            }.value,
                            modifier = Modifier.size(75.dp),
                            contentDescription = "Album Cover Art"
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                            Text(
                                text = albumDetailScreenState.albumTitle,
                                style = MaterialTheme.typography.titleLarge,
                                color = contentColorFor(detailsViewModel.previewCardColor.value),
                                fontSize = 14.sp,
                                softWrap = true,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 15.dp)
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                        append("Preview")
                                    }
                                    append(" • ${albumDetailScreenState.itemType}")
                                },
                                style = MaterialTheme.typography.titleSmall,
                                color = contentColorFor(detailsViewModel.previewCardColor.value).copy(
                                    0.9f
                                ),
                                softWrap = true,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                            Row {
                                IconButton(onClick = {
                                    mediaPlayer.stop()
                                    mediaPlayer.reset()
                                    detailsViewModel.albumScreenState = albumDetailScreenState
                                    navController.navigate(NavigationRoutes.VIDEO_CANVAS.name)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.MusicVideo,
                                        contentDescription = "Play Album Preview Icon Button"
                                    )
                                }
                                Spacer(modifier = Modifier.width(15.dp))
                            }
                        }
                    }
                }
            }
            item {
                Divider(
                    modifier = Modifier.padding(start = 15.dp, end = 15.dp),
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
                HorizontalTrackPreview(
                    trackNumber = item.track_number.toString(),
                    trackName = item.name,
                    isExplicit = item.explicit,
                    artists = item.artists.map { it.name },
                    currentTrackID = item.id,
                    selectedTrackId = selectedTrackId.value,
                    isAnyTrackIsPlayingState = isAnyTrackIsPlayingState,
                    isAnyTrackInLoadingState = isAnyTrackInLoadingState,
                    onPlayClick = {
                        if (item.id == selectedTrackId.value && (isAnyTrackIsPlayingState.value || isAnyTrackInLoadingState.value)) {
                            isAnyTrackInLoadingState.value = false
                            isAnyTrackIsPlayingState.value = false
                            mediaPlayer.stop()
                            mediaPlayer.reset()
                            return@HorizontalTrackPreview
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
                            isAnyTrackInLoadingState.value = false
                            isAnyTrackIsPlayingState.value = true
                        }
                        mediaPlayer.setOnCompletionListener {
                            it.stop()
                            it.reset()
                            isAnyTrackIsPlayingState.value = false
                            isAnyTrackInLoadingState.value = false
                        }
                    },
                    currentPlayingTrackDurationAsFloat = currentPlayingTrackDurationAsFloat
                )
            }
            item {
                Spacer(modifier = Modifier.height(200.dp))
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
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth()
                ) {
                    AlbumxTrackCover(
                        albumxTrackCoverState = AlbumxTrackCoverState(
                            covertImgUrl = albumDetailScreenState.covertArtImgUrl.collectAsState(
                                initial = ""
                            ).value,
                            mainImgUrl = albumDetailScreenState.albumImgUrl,
                            itemTitle = albumDetailScreenState.albumTitle,
                            itemArtists = albumDetailScreenState.artists,
                            itemType = "Album"
                        ),
                        detailsViewModel = detailsViewModel,
                        navController = navController,
                    )
                    Text(
                        text = albumDetailScreenState.albumWiki.collectAsState(initial = "").value,
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
                            text = "Info straight outta LastFM",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
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