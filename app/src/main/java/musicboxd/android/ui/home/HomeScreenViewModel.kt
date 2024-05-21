package musicboxd.android.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import musicboxd.android.data.remote.api.APIResult
import musicboxd.android.data.remote.api.musicboxd.MusicBoxdAPIRepo
import musicboxd.android.data.remote.api.musicboxd.model.review.MusicBoxdPublicReviews
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val musicBoxdAPIRepo: MusicBoxdAPIRepo) :
    ViewModel() {
    private val _publicReviews = MutableStateFlow(listOf<MusicBoxdPublicReviews>())
    val publicReviews = _publicReviews.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            when (val reviewsData = musicBoxdAPIRepo.getReviews()) {
                is APIResult.Failure -> {
                    Log.d("10MinMail", reviewsData.message)
                }

                is APIResult.Success -> {
                    _publicReviews.emit(reviewsData.data.reversed())
                }
            }
        }
    }
}