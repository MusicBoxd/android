package musicboxd.android.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import musicboxd.android.data.remote.scrape.artist.tour.ArtistTourRepo
import musicboxd.android.data.remote.scrape.artist.tour.model.ArtistTourDTO
import javax.inject.Inject

@HiltViewModel
class NotificationsScreenViewModel @Inject constructor(private val artistTourRepo: ArtistTourRepo) :
    ViewModel() {

    private val _sampleList = MutableStateFlow(emptyList<ArtistTourDTO>())
    val sampleList = _sampleList.asStateFlow()

    init {
        retrieveLatestConcertsData()
    }

    private fun retrieveLatestConcertsData() {
        viewModelScope.launch {
            artistTourRepo.getEventDetails("7pjPlcQp4vjwDGvuzQs5pz")
        }
    }
}