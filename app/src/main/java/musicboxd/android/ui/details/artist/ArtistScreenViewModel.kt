package musicboxd.android.ui.details.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import musicboxd.android.data.local.events.EventRepo
import musicboxd.android.data.local.events.model.Event
import javax.inject.Inject

@HiltViewModel
class ArtistScreenViewModel @Inject constructor(private val eventRepo: EventRepo) : ViewModel() {
    private val _doesEventExists = MutableStateFlow<Boolean?>(null)
    val doesEventExists = _doesEventExists.asStateFlow()

    fun onUIEvent(artistDetailsScreenUiEvent: ArtistDetailsScreenUiEvent) {
        when (artistDetailsScreenUiEvent) {
            is ArtistDetailsScreenUiEvent.DeleteAnEvent -> {
                deleteAnEvent(artistDetailsScreenUiEvent.eventId)
            }

            is ArtistDetailsScreenUiEvent.DoesEventExist -> {
                doesEventExist(artistDetailsScreenUiEvent.eventId)
            }

            is ArtistDetailsScreenUiEvent.SaveAnEvent -> {
                saveAnEvent(artistDetailsScreenUiEvent.event)
            }
        }
    }

    private fun saveAnEvent(event: Event) {
        viewModelScope.launch {
            eventRepo.saveANewEvent(event)
        }
    }

    private fun deleteAnEvent(eventUri: String) {
        viewModelScope.launch {
            eventRepo.deleteAnExistingEvent(eventUri)
        }
    }

    private fun doesEventExist(eventUri: String) {
        _doesEventExists.value = null
        viewModelScope.launch {
            eventRepo.doesEventExist(eventUri).collectLatest {
                _doesEventExists.emit(it)
            }
        }
    }
}