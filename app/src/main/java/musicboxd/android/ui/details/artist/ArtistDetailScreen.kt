package musicboxd.android.ui.details.artist

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import musicboxd.android.data.remote.api.spotify.model.album.Albums
import musicboxd.android.data.remote.api.spotify.model.topTracks.TopTracksDTO
import musicboxd.android.ui.common.ArtistCoverArt
import musicboxd.android.ui.common.ArtistCoverArtState
import musicboxd.android.ui.common.CoilImage
import musicboxd.android.ui.common.HorizontalTrackPreview
import musicboxd.android.ui.common.fadedEdges
import musicboxd.android.ui.details.DetailsViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ArtistDetailScreen(detailsViewModel: DetailsViewModel) {
    val topTracks = detailsViewModel.topTracksDTO.collectAsStateWithLifecycle(
        initialValue = TopTracksDTO(
            emptyList()
        )
    )
    val albums = detailsViewModel.artistAlbums.collectAsStateWithLifecycle(
        initialValue = Albums(
            items = listOf(),
            limit = 0,
            offset = 0,
            total = 0
        )
    ).value.items
    val specificArtistFromSpotifyDTO = detailsViewModel.artistInfo
    val isGenresExpanded = rememberSaveable {
        mutableStateOf(false)
    }
    val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()
    val mediaPlayer = remember {
        MediaPlayer().apply {
            setAudioAttributes(audioAttributes)
        }
    }
    val colorScheme = MaterialTheme.colorScheme
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
    val artistBio = detailsViewModel.artistBio.collectAsStateWithLifecycle()
    val localContext = LocalContext.current
    val lastFmImage = detailsViewModel.lastFMImage.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = isAnyTrackIsPlayingState.value) {
        while (isAnyTrackIsPlayingState.value) {
            currentPlayingTrackDurationAsFloat.floatValue =
                mediaPlayer.currentPosition.toFloat() / 30000f
            delay(500L)
        }
    }
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            ArtistCoverArt(
                artistCoverArtState =
                ArtistCoverArtState(
                    artistName = specificArtistFromSpotifyDTO.value.name,
                    artistImgUrl = if (specificArtistFromSpotifyDTO.value.images.isNotEmpty()) specificArtistFromSpotifyDTO.value.images.first().url else ""
                )
            )
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        isGenresExpanded.value = !isGenresExpanded.value
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Genres",
                    style = MaterialTheme.typography.titleMedium,
                    color = LocalContentColor.current,
                    modifier = Modifier.padding(start = 10.dp)
                )
                IconButton(onClick = {
                    isGenresExpanded.value = !isGenresExpanded.value
                }) {
                    Icon(
                        imageVector = if (!isGenresExpanded.value) Icons.Default.ExpandMore else Icons.Default.ExpandLess,
                        contentDescription = null
                    )
                }
            }
            Box(modifier = Modifier.animateContentSize()) {
                if (isGenresExpanded.value) {
                    FlowRow(
                        modifier = Modifier
                            .padding(start = 10.dp, bottom = 10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        specificArtistFromSpotifyDTO.value.genres.forEach {
                            AssistChip(onClick = {
                            }, label = {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = LocalContentColor.current,
                                    modifier = Modifier.padding(10.dp)
                                )
                            })
                        }
                    }
                }
            }
        }
        item {
            Text(
                text = "Top Tracks",
                style = MaterialTheme.typography.titleMedium,
                color = LocalContentColor.current,
                modifier = Modifier.padding(start = 10.dp, bottom = 5.dp)
            )
        }
        items(topTracks.value.tracks, key = {
            it.id
        }) {
            HorizontalTrackPreview(
                trackNumber = it.track_number.toString(),
                trackName = it.name,
                isExplicit = it.explicit,
                artists = it.artists.map { it.name },
                currentTrackID = it.id,
                selectedTrackId = selectedTrackId.value,
                isAnyTrackIsPlayingState = isAnyTrackIsPlayingState,
                isAnyTrackInLoadingState = isAnyTrackInLoadingState,
                onPlayClick = {
                    if (it.id == selectedTrackId.value && (isAnyTrackIsPlayingState.value || isAnyTrackInLoadingState.value)) {
                        isAnyTrackInLoadingState.value = false
                        isAnyTrackIsPlayingState.value = false
                        mediaPlayer.stop()
                        mediaPlayer.reset()
                        return@HorizontalTrackPreview
                    }
                    Toast
                        .makeText(localContext, "Fetching Audio", Toast.LENGTH_SHORT)
                        .show()
                    selectedTrackId.value = it.id
                    isAnyTrackInLoadingState.value = true
                    mediaPlayer.stop()
                    mediaPlayer.reset()
                    mediaPlayer.setDataSource(it.preview_url)
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
                currentPlayingTrackDurationAsFloat = currentPlayingTrackDurationAsFloat,
                trackImgUrl = it.album.images.first().url
            )
        }
        item {
            Text(
                text = "Discography",
                style = MaterialTheme.typography.titleMedium,
                color = LocalContentColor.current,
                modifier = Modifier.padding(10.dp)
            )
        }
        item {
            Spacer(modifier = Modifier.height(5.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Spacer(modifier = Modifier.width(0.dp))
                }
                items(albums, key = {
                    it.id
                }) {
                    Column(
                        Modifier
                            .width(200.dp)
                            .height(275.dp)
                    ) {
                        CoilImage(
                            imgUrl = it.images.first().url,
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            contentDescription = "${it.name} cover art"
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = LocalContentColor.current,
                            fontSize = 18.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = it.album_type.capitalize(),
                            style = MaterialTheme.typography.titleSmall,
                            color = LocalContentColor.current.copy(0.85f),
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.width(0.dp))
                }
            }
        }
        item {
            FilledTonalButton(modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp), onClick = { }) {
                Text(
                    text = "See discography",
                    color = LocalContentColor.current,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }

        item {
            Text(
                text = "About",
                style = MaterialTheme.typography.titleMedium,
                color = LocalContentColor.current,
                modifier = Modifier.padding(10.dp)
            )
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                CoilImage(
                    imgUrl = lastFmImage.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .fadedEdges(colorScheme)
                        .fadedEdges(colorScheme),
                    contentDescription = "${specificArtistFromSpotifyDTO.value.name} Wiki"
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = artistBio.value.trim()
                            .replace("\n", "\n\n") + if (artistBio.value.endsWith(".")) "" else ".",
                        style = MaterialTheme.typography.titleSmall,
                        color = LocalContentColor.current,
                        maxLines = 5,
                        fontSize = 16.sp,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .padding(10.dp)
                    )
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "")
                    }
                }
            }
        }
    }
}