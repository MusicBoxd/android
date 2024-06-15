package musicboxd.android.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import musicboxd.android.data.local.events.EventDao
import musicboxd.android.data.local.events.model.Event
import javax.inject.Inject

@HiltViewModel
class SavedEventsScreenVM @Inject constructor(private val eventDao: EventDao) : ViewModel() {
    private val _savedEventsData = MutableStateFlow(emptyList<Event>())
    val savedEventsData = _savedEventsData.asStateFlow()

    init {
        viewModelScope.launch {
            eventDao.getAllSavedEvents().collectLatest {
                _savedEventsData.emit(it)
            }
        }
    }

}