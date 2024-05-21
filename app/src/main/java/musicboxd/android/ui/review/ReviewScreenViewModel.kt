package musicboxd.android.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import musicboxd.android.TEMP_PASSWORD
import musicboxd.android.TEMP_USER_NAME
import musicboxd.android.data.remote.api.APIResult
import musicboxd.android.data.remote.api.musicboxd.MusicBoxdAPIRepo
import musicboxd.android.data.remote.api.musicboxd.model.MusicBoxdLoginDTO
import musicboxd.android.data.remote.api.musicboxd.model.ReviewDTO
import javax.inject.Inject

@HiltViewModel
class ReviewScreenViewModel @Inject constructor(private val musicBoxdAPIRepo: MusicBoxdAPIRepo) :
    ViewModel() {

    private val _reviewScreenUIChannel = Channel<ReviewScreenUIEvent>()
    val reviewScreenUIChannel = _reviewScreenUIChannel.consumeAsFlow()

    fun postANewReview(reviewDTO: ReviewDTO) {
        viewModelScope.launch(Dispatchers.Default) {
            when (val tokenData = musicBoxdAPIRepo.getUserToken(
                MusicBoxdLoginDTO(
                    userName = TEMP_USER_NAME,
                    password = TEMP_PASSWORD
                )
            )) {
                is APIResult.Failure -> TODO()
                is APIResult.Success -> {
                    when (val postedReview =
                        musicBoxdAPIRepo.postANewReview(reviewDTO, tokenData.data.jwt)) {
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

    private suspend fun sendAnEvent(reviewScreenUIEvent: ReviewScreenUIEvent) {
        _reviewScreenUIChannel.send(reviewScreenUIEvent)
    }
}