package musicboxd.android.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import musicboxd.android.data.local.review.ReviewRepo
import musicboxd.android.data.local.review.model.Review
import musicboxd.android.data.local.user.UserRepo
import musicboxd.android.data.remote.api.APIResult
import musicboxd.android.data.remote.api.musicboxd.MusicBoxdAPIRepo
import musicboxd.android.data.remote.api.musicboxd.model.MusicBoxdLoginDTO
import musicboxd.android.data.remote.api.musicboxd.model.ReviewDTO
import musicboxd.android.data.remote.api.spotify.model.tracklist.Artist
import musicboxd.android.ui.details.album.AlbumDetailScreenState
import javax.inject.Inject

@HiltViewModel
class ReviewScreenViewModel @Inject constructor(
    private val musicBoxdAPIRepo: MusicBoxdAPIRepo,
    private val reviewRepo: ReviewRepo,
    private val userRepo: UserRepo
) :
    ViewModel() {

    private val _reviewScreenUIChannel = Channel<ReviewScreenUIEvent>()
    val reviewScreenUIChannel = _reviewScreenUIChannel.receiveAsFlow()

    val reviewTitle = MutableStateFlow("")
    val reviewContent = MutableStateFlow("")
    val albumLikeStatus = MutableStateFlow(false)
    val albumRecommendationStatus = MutableStateFlow(false)
    val albumRatingValue = MutableStateFlow(0f)
    val reviewTags = MutableStateFlow("")
    var currentLocalReview = Review(
        localReviewId = -1,
        remoteReviewId = 0L,
        releaseType = "",
        releaseName = "",
        artists = listOf(),
        spotifyUri = "",
        isExplicit = false,
        reviewContent = "",
        isLiked = false,
        isRecommended = false,
        gotAddedIntoAnyLists = false,
        listIds = listOf(),
        reviewedByUserID = 0L,
        reviewedOnDate = "",
        reviewTitle = "",
        rating = 0.0f,
        timeStamp = 0L,
        genres = listOf(),
        noOfLikesForThisReview = 0L,
        reviewTags = listOf(),
        releaseImgUrl = ""
    )

    init {
        val reviewData = object {
            var reviewTitle = ""
            var reviewContent = ""
            var isLiked = false
            var isRecommended = false
            var tags = ""
        }
        viewModelScope.launch {
            combine(
                reviewTitle, reviewContent,
                albumLikeStatus,
                albumRecommendationStatus, reviewTags
            ) { title, review, isLiked, isRecommended, tags ->
                reviewData.reviewTitle = title
                reviewData.reviewContent = review
                reviewData.isLiked = isLiked
                reviewData.isRecommended = isRecommended
                reviewData.tags = tags
                return@combine reviewData
            }.collectLatest {
                if (currentLocalReview.localReviewId >= 0) {
                    updateAnExistingLocalReview(
                        currentLocalReview.copy(
                            reviewTags = it.tags.split(",").map { it.trim() },
                            reviewTitle = it.reviewTitle,
                            reviewContent = it.reviewContent,
                            isLiked = it.isLiked,
                            isRecommended = it.isRecommended
                        )
                    )
                }
            }
        }
    }

    fun postANewReview(reviewDTO: ReviewDTO) {
        viewModelScope.launch(Dispatchers.Default) {
            when (val userToken = musicBoxdAPIRepo.getUserToken(
                MusicBoxdLoginDTO(
                    userName = userRepo.getUserData().userName,
                    password = userRepo.getUserData().password
                )
            )) {
                is APIResult.Failure -> TODO()
                is APIResult.Success -> {
                    when (val postedReview =
                        musicBoxdAPIRepo.postANewReview(reviewDTO, userToken.data.jwt)) {
                        is APIResult.Failure -> {
                            sendAnEvent(ReviewScreenUIEvent.ShowToast(postedReview.message))
                        }

                        is APIResult.Success -> {
                            viewModelScope.launch {
                                sendAnEvent(ReviewScreenUIEvent.ShowToast(postedReview.data))
                            }
                        }
                    }
                }
            }

        }
    }

    fun createANewLocalReview(albumDetailScreenState: AlbumDetailScreenState) {
        viewModelScope.launch {
            if (!reviewRepo.doesThisReviewExistsOnLocalDevice(albumDetailScreenState.itemUri)) {
                reviewRepo.createANewLocalReview(
                    Review(
                        remoteReviewId = 0L,
                        releaseType = albumDetailScreenState.itemType,
                        releaseName = albumDetailScreenState.albumTitle,
                        artists = albumDetailScreenState.artists.map {
                            Artist(
                                id = it.id,
                                name = it.name,
                                uri = it.uri
                            )
                        },
                        spotifyUri = albumDetailScreenState.itemUri,
                        isExplicit = false,
                        reviewContent = "",
                        isLiked = false,
                        isRecommended = false,
                        gotAddedIntoAnyLists = false,
                        listIds = listOf(),
                        reviewedByUserID = 0L,
                        reviewedOnDate = "",
                        reviewTitle = reviewTitle.value,
                        rating = 0.0f,
                        timeStamp = 0L,
                        genres = listOf(),
                        noOfLikesForThisReview = 0L,
                        reviewTags = listOf(),
                        releaseImgUrl = albumDetailScreenState.albumImgUrl
                    )
                )
                this.launch {
                    reviewRepo.getTheLatestAddedLocalReview().let {
                        reviewTitle.value = it.reviewTitle
                        reviewContent.value = it.reviewContent
                        albumLikeStatus.value = it.isLiked
                        albumRecommendationStatus.value = it.isRecommended
                        albumRatingValue.value = it.rating
                        reviewTags.value =
                            it.reviewTags.joinToString { it }.trim()
                    }
                }
                this.launch {
                    reviewRepo.getTheLatestAddedLocalReviewAsFlow().collectLatest {
                        currentLocalReview = it
                    }
                }
            }
        }
    }

    fun loadExistingLocalReview(itemUri: String) {
        viewModelScope.launch {
            if (reviewRepo.doesThisReviewExistsOnLocalDevice(itemUri)) {
                this.launch {
                    reviewRepo.getASpecificLocalReview(itemUri).let {
                        reviewTitle.value = it.reviewTitle
                        reviewContent.value = it.reviewContent
                        albumLikeStatus.value = it.isLiked
                        albumRecommendationStatus.value = it.isRecommended
                        albumRatingValue.value = it.rating
                        reviewTags.value =
                            it.reviewTags.joinToString { it }.trim()
                    }
                }
                this.launch {
                    reviewRepo.getASpecificLocalReviewAsFlow(itemUri)
                        .collectLatest {
                            currentLocalReview = it
                        }
                }
            }
        }
    }

    fun updateAnExistingLocalReview(review: Review) {
        viewModelScope.launch {
            reviewRepo.updateAnExistingLocalReview(review)
        }
    }

    private suspend fun sendAnEvent(reviewScreenUIEvent: ReviewScreenUIEvent) {
        _reviewScreenUIChannel.send(reviewScreenUIEvent)
    }
}