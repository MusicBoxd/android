package musicboxd.android.ui.lists

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
import musicboxd.android.TEMP_PASSWORD
import musicboxd.android.TEMP_USER_NAME
import musicboxd.android.data.local.list.ListRepo
import musicboxd.android.data.local.list.model.List
import musicboxd.android.data.local.list.model.MusicContent
import musicboxd.android.data.remote.api.APIResult
import musicboxd.android.data.remote.api.musicboxd.MusicBoxdAPIRepo
import musicboxd.android.data.remote.api.musicboxd.model.MusicBoxdLoginDTO
import musicboxd.android.data.remote.api.musicboxd.model.list.ListDTO
import musicboxd.android.ui.details.album.AlbumDetailScreenState
import javax.inject.Inject

@HiltViewModel
class CreateANewListScreenViewModel @Inject constructor(
    private val musicBoxdAPIRepo: MusicBoxdAPIRepo,
    private val listRepo: ListRepo
) : ViewModel() {
    val currentMusicContentSelection = MutableStateFlow(emptyList<AlbumDetailScreenState>())

    private val _uiEvent = Channel<CreateANewListScreenUIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val newListTitle = MutableStateFlow("")
    val newListDescription = MutableStateFlow("")
    val isAPublicList = MutableStateFlow(false)

    val isANumberedList = MutableStateFlow(false)

    private var currentModifyingList = List(
        localId = -1,
        remoteId = 0L,
        nameOfTheList = "",
        descriptionOfTheList = "",
        isAPublicList = false,
        isANumberedList = false,
        musicContent = listOf(),
        dateCreated = "",
        timeStamp = "",
        noOfLikes = 0L
    )

    fun setValuesToDefault() {
        currentMusicContentSelection.value = emptyList()
        currentModifyingList = List(
            localId = -1,
            remoteId = 0L,
            nameOfTheList = "",
            descriptionOfTheList = "",
            isAPublicList = false,
            isANumberedList = false,
            musicContent = listOf(),
            dateCreated = "",
            timeStamp = "",
            noOfLikes = 0L
        )
        newListTitle.value = ""
        newListDescription.value = ""
        isAPublicList.value = false
        isANumberedList.value = false
    }

    init {
        val listData = object {
            var listTitle: String = ""
            var listDesc: String = ""
            var isAPublicList: Boolean = false
            var isNumberedList: Boolean = false
            var currentMusicSelection = emptyList<AlbumDetailScreenState>()
        }
        viewModelScope.launch() {
            combine(currentMusicContentSelection) {
                it
            }.collectLatest {
                if (currentMusicContentSelection.value.isNotEmpty() && currentModifyingList.localId == (-1).toLong()) {
                    createALocalDraftList()
                }
            }
        }
        viewModelScope.launch {
            combine(
                newListTitle,
                newListDescription,
                isAPublicList,
                isANumberedList,
                currentMusicContentSelection
            ) { title, desc, isAPublicList, isANumberedList, musicContent ->
                listData.listTitle = title
                listData.listDesc = desc
                listData.isNumberedList = isANumberedList
                listData.isAPublicList = isAPublicList
                listData.currentMusicSelection = musicContent
            }.collectLatest {
                if (currentModifyingList.localId != (-1).toLong() && currentMusicContentSelection.value.isNotEmpty()) {
                    listRepo.updateAnExistingLocalList(
                        currentModifyingList.copy(nameOfTheList = listData.listTitle,
                            descriptionOfTheList = listData.listDesc,
                            isANumberedList = listData.isNumberedList,
                            isAPublicList = listData.isAPublicList,
                            musicContent = listData.currentMusicSelection.map {
                                MusicContent(
                                    itemName = it.albumTitle,
                                    itemImgUrl = it.albumImgUrl,
                                    itemSpotifyUri = it.itemUri
                                )
                            })
                    )
                }
            }
        }
    }

    private fun createALocalDraftList() {
        viewModelScope.launch {
            listRepo.createANewLocalList(
                List(
                    remoteId = 0,
                    nameOfTheList = newListTitle.value,
                    descriptionOfTheList = newListDescription.value,
                    isAPublicList = isAPublicList.value,
                    isANumberedList = isANumberedList.value,
                    musicContent = currentMusicContentSelection.value.map {
                        MusicContent(
                            itemName = it.albumTitle,
                            itemImgUrl = it.albumImgUrl,
                            itemSpotifyUri = it.itemUri
                        )
                    },
                    dateCreated = "",
                    timeStamp = "",
                    noOfLikes = 0
                )
            )
        }.invokeOnCompletion {
            loadAnExistingLocalDraftList()
        }
    }

    private fun loadAnExistingLocalDraftList() {
        viewModelScope.launch {
            this.launch {
                listRepo.getLatestList().collectLatest {
                    currentModifyingList = it
                }
            }
        }
    }

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
                            spotifyURIs = currentMusicContentSelection.value
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