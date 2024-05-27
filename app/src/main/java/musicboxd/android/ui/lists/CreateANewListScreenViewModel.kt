package musicboxd.android.ui.lists

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import musicboxd.android.TEMP_PASSWORD
import musicboxd.android.TEMP_USER_NAME
import musicboxd.android.data.remote.api.APIResult
import musicboxd.android.data.remote.api.musicboxd.MusicBoxdAPIRepo
import musicboxd.android.data.remote.api.musicboxd.model.MusicBoxdLoginDTO
import musicboxd.android.data.remote.api.musicboxd.model.list.ListDTO
import musicboxd.android.ui.details.album.AlbumDetailScreenState
import javax.inject.Inject

@HiltViewModel
class CreateANewListScreenViewModel @Inject constructor(private val musicBoxdAPIRepo: MusicBoxdAPIRepo) :
    ViewModel() {
    val currentSelection = mutableStateOf(emptyList<AlbumDetailScreenState>())

    private val _uiEvent = Channel<CreateANewListScreenUIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun publishANewList(listName: String, listDescription: String, isListPublic: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            when (val tokenData = musicBoxdAPIRepo.getUserToken(
                MusicBoxdLoginDTO(
                    userName = TEMP_USER_NAME, password = TEMP_PASSWORD
                )
            )) {
                is APIResult.Failure -> TODO()
                is APIResult.Success -> {
                    when (musicBoxdAPIRepo.postANewList(
                        ListDTO(listName = listName,
                            lisDescription = listDescription,
                            isListPublic = isListPublic,
                            spotifyURIs = currentSelection.value
                                .map { it.itemUri }),
                        tokenData.data.jwt
                    )) {
                        is APIResult.Failure -> {
                            pushUiEvent(CreateANewListScreenUIEvent.ShowToast("Failure"))
                        }

                        is APIResult.Success -> {
                            pushUiEvent(CreateANewListScreenUIEvent.ShowToast("Success"))
                        }
                    }
                }
            }
        }

    }

    private suspend fun pushUiEvent(createANewListScreenUIEvent: CreateANewListScreenUIEvent) {
        _uiEvent.send(createANewListScreenUIEvent)
    }
}