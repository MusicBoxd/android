package musicboxd.android.ui.review

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Recommend
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Recommend
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle
import com.gowtham.ratingbar.StepSize
import kotlinx.coroutines.flow.collectLatest
import musicboxd.android.data.remote.api.musicboxd.model.ReviewDTO
import musicboxd.android.ui.common.CoilImage
import musicboxd.android.ui.details.DetailsViewModel
import musicboxd.android.ui.theme.fonts
import musicboxd.android.utils.musicBoxdLog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddANewReviewScreen(
    navController: NavController,
    detailsViewModel: DetailsViewModel,
) {
    val reviewScreenViewModel: ReviewScreenViewModel = hiltViewModel()
    val uiChannel = reviewScreenViewModel.reviewScreenUIChannel
    val localContext = LocalContext.current
    LaunchedEffect(key1 = reviewScreenViewModel.reviewTitle.collectAsStateWithLifecycle().value) {
        if (reviewScreenViewModel.currentLocalReview.localReviewId == (-1).toLong() && reviewScreenViewModel.reviewTitle.value.isNotEmpty()) {
            reviewScreenViewModel.createANewLocalReview(detailsViewModel.albumScreenState)
        }
    }
    LaunchedEffect(key1 = Unit) {
        reviewScreenViewModel.loadExistingLocalReview(
            detailsViewModel.albumScreenState.itemUri
        )
    }
    LaunchedEffect(key1 = Unit) {
        uiChannel.collectLatest {
            when (it) {
                is ReviewScreenUIEvent.ShowToast -> {
                    Toast.makeText(localContext, it.toastMessage, Toast.LENGTH_SHORT).show()
                }

                is ReviewScreenUIEvent.Nothing -> TODO()
            }
        }
    }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = ""
                )
            }
        }, title = {
            Text(
                text = "Write a new Review",
                fontSize = 16.sp,
                style = MaterialTheme.typography.titleLarge

            )
        })
    }, bottomBar = {
        BottomAppBar {
            Button(
                onClick = {
                    musicBoxdLog(reviewScreenViewModel.currentLocalReview.spotifyUri)
                    reviewScreenViewModel.postANewReview(
                        ReviewDTO(
                            reviewContent = reviewScreenViewModel.reviewContent.value,
                            itemUri = reviewScreenViewModel.currentLocalReview.spotifyUri,
                            reviewRating = reviewScreenViewModel.albumRatingValue.value,
                            reviewTitle = reviewScreenViewModel.reviewTitle.value
                        )
                    )
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Text(text = "Post Review", style = MaterialTheme.typography.titleSmall)
            }
        }
    }) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            ReviewUI(detailsViewModel = detailsViewModel, reviewScreenViewModel)
        }
    }
}

@Composable
private fun ReviewUI(
    detailsViewModel: DetailsViewModel,
    reviewScreenViewModel: ReviewScreenViewModel
) {
    val selectedAlbumData = detailsViewModel.albumScreenState
    val albumLikeStatus = reviewScreenViewModel.albumLikeStatus.collectAsStateWithLifecycle()
    val albumRecommendationStatus =
        reviewScreenViewModel.albumRecommendationStatus.collectAsStateWithLifecycle()
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoilImage(
            imgUrl = selectedAlbumData.albumImgUrl,
            modifier = Modifier
                .padding(15.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(15.dp)),
            contentDescription = ""
        )
        Column {
            Text(
                text = selectedAlbumData.albumTitle,
                fontSize = 18.sp,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp),
                fontWeight = FontWeight.Black,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = selectedAlbumData.artists.map { it.name }.joinToString { it },
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp),
                color = LocalContentColor.current.copy(0.8f)
            )
        }
    }
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp)
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            if (albumLikeStatus.value) {
                FilledTonalIconButton(onClick = {
                    reviewScreenViewModel.albumLikeStatus.value =
                        !reviewScreenViewModel.albumLikeStatus.value
                }) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = ""
                    )
                }
            } else {
                IconButton(onClick = {
                    reviewScreenViewModel.albumLikeStatus.value =
                        !reviewScreenViewModel.albumLikeStatus.value
                }) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = ""
                    )
                }
            }
            Spacer(modifier = Modifier.width(5.dp))
            if (albumRecommendationStatus.value) {
                FilledTonalIconButton(onClick = {
                    reviewScreenViewModel.albumRecommendationStatus.value =
                        !reviewScreenViewModel.albumRecommendationStatus.value
                }) {
                    Icon(
                        imageVector = Icons.Filled.Recommend,
                        contentDescription = ""
                    )
                }
            } else {
                IconButton(onClick = {
                    reviewScreenViewModel.albumRecommendationStatus.value =
                        !reviewScreenViewModel.albumRecommendationStatus.value
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Recommend,
                        contentDescription = ""
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Spacer(
            modifier = Modifier
                .width(1.5.dp)
                .height(15.dp)
                .background(MaterialTheme.colorScheme.onSurface)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            RatingBar(
                modifier = Modifier.padding(start = 15.dp),
                config = RatingBarConfig().inactiveColor(MaterialTheme.colorScheme.outline)
                    .activeColor(MaterialTheme.colorScheme.primary).padding(5.dp)
                    .stepSize(StepSize.HALF).style(RatingBarStyle.HighLighted),
                value = reviewScreenViewModel.albumRatingValue.collectAsStateWithLifecycle().value,
                onValueChange = {
                    reviewScreenViewModel.albumRatingValue.value = it
                },
                onRatingChanged = {
                    reviewScreenViewModel.updateAnExistingLocalReview(
                        reviewScreenViewModel.currentLocalReview.copy(
                            rating = it
                        )
                    )
                })
        }

    }
    Spacer(modifier = Modifier.height(15.dp))
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp),
        value = reviewScreenViewModel.reviewTitle.collectAsStateWithLifecycle().value,
        onValueChange = { newValue ->
            reviewScreenViewModel.reviewTitle.value = newValue
        },
        textStyle = TextStyle(
            fontFamily = fonts,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = LocalContentColor.current
        ),
        label = {
            Text(
                text = "Review Title",
                style = MaterialTheme.typography.titleSmall
            )
        }
    )
    Spacer(modifier = Modifier.height(15.dp))
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 100.dp)
            .padding(start = 15.dp, end = 15.dp),
        value = reviewScreenViewModel.reviewContent.collectAsStateWithLifecycle().value,
        onValueChange = { newValue ->
            reviewScreenViewModel.reviewContent.value = newValue
        },
        textStyle = TextStyle(
            fontFamily = fonts,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = LocalContentColor.current
        ),
        label = {
            Text(
                text = "Review",
                style = MaterialTheme.typography.titleSmall
            )
        }
    )
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp), color = MaterialTheme.colorScheme.outline.copy(0.5f)
    )
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.Info, contentDescription = "")
        Spacer(modifier = Modifier.width(15.dp))
        Text(
            text = buildAnnotatedString {
                append("Your review is automatically saved on your device for editing in ")
                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                    append("Drafts")
                }
                append("; Once posted, it cannot be changed.")
            },
            style = MaterialTheme.typography.titleSmall
        )
    }
    Spacer(modifier = Modifier.height(15.dp))
}