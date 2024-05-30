package musicboxd.android.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import musicboxd.android.data.local.model.review.Review
import musicboxd.android.data.local.review.ReviewRepo
import javax.inject.Inject

@HiltViewModel
class AddScreenViewModel @Inject constructor(
    private val reviewRepo: ReviewRepo
) : ViewModel() {
    private val _localReviews = MutableStateFlow(emptyList<Review>())
    val localReviews = _localReviews.asStateFlow()

    fun loadLocalReviews() {
        viewModelScope.launch {
            reviewRepo.getAllExistingLocalReviews().collectLatest {
                _localReviews.emit(it)
            }
        }
    }
}