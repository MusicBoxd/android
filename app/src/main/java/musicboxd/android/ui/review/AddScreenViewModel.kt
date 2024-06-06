package musicboxd.android.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import musicboxd.android.data.local.list.ListRepo
import musicboxd.android.data.local.list.model.List
import musicboxd.android.data.local.review.ReviewRepo
import musicboxd.android.data.local.review.model.Review
import javax.inject.Inject

@HiltViewModel
class AddScreenViewModel @Inject constructor(
    private val reviewRepo: ReviewRepo,
    private val listRepo: ListRepo
) : ViewModel() {
    private val _localReviews = MutableStateFlow(emptyList<Review>())
    val localReviews = _localReviews.asStateFlow()

    private val _localLists = MutableStateFlow(emptyList<List>())
    val localLists = _localLists.asStateFlow()

    init {
        viewModelScope.launch {
            loadLocalLists()
        }
        viewModelScope.launch {
            loadLocalReviews()
        }
    }

    private suspend fun loadLocalLists() {
        listRepo.getAllLocalListsAsFlow().collectLatest {
            _localLists.emit(it)
        }
    }

    private suspend fun loadLocalReviews() {
        reviewRepo.getAllExistingLocalReviews().collectLatest {
            _localReviews.emit(it)
        }
    }
}