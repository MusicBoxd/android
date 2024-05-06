package musicboxd.android.ui.details.artist

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.AssistChip
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import musicboxd.android.ui.common.ArtistCoverArt
import musicboxd.android.ui.common.ArtistCoverArtState
import musicboxd.android.ui.common.CoilImage
import musicboxd.android.ui.common.HorizontalTrackPreview
import musicboxd.android.ui.details.DetailsViewModel

@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@Composable
fun ArtistDetailScreen(detailsViewModel: DetailsViewModel) {
    val spotifyData = detailsViewModel.artistDetailScreenState
    val topTracks = spotifyData.value.topTracks.tracks
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
    val localContext = LocalContext.current
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
                    artistName = spotifyData.value.specificArtistFromSpotifyDTO.name,
                    artistImgUrl = if (spotifyData.value.specificArtistFromSpotifyDTO.images.isNotEmpty()) spotifyData.value.specificArtistFromSpotifyDTO.images.first().url else ""
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
                        detailsViewModel.artistDetailScreenState.value.specificArtistFromSpotifyDTO.genres.forEach {
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
        items(topTracks, key = {
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
                text = "Albums",
                style = MaterialTheme.typography.titleMedium,
                color = LocalContentColor.current,
                modifier = Modifier.padding(10.dp)
            )
        }
        item {
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(spotifyData.value.albumSearchDTO.items, key = {
                    it.id
                }) {
                    CoilImage(
                        imgUrl = it.images.first().url,
                        modifier = Modifier.size(250.dp),
                        contentDescription = "${it.name} cover art"
                    )
                }
            }
        }
    }
}