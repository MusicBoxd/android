package musicboxd.android.ui.lists

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import musicboxd.android.data.local.list.ListRepo
import musicboxd.android.data.local.list.model.List
import musicboxd.android.data.local.list.model.MusicContent
import musicboxd.android.data.local.user.UserRepo
import musicboxd.android.data.remote.api.APIResult
import musicboxd.android.data.remote.api.musicboxd.MusicBoxdAPIRepo
import musicboxd.android.data.remote.api.musicboxd.model.list.ListDTO
import musicboxd.android.ui.details.album.AlbumDetailScreenState
import javax.inject.Inject

@HiltViewModel
class CreateANewListScreenViewModel @Inject constructor(
    private val musicBoxdAPIRepo: MusicBoxdAPIRepo,
    private val listRepo: ListRepo,
    private val userRepo: UserRepo
) : ViewModel() {
    val currentMusicContentSelection = mutableStateListOf<AlbumDetailScreenState>()

    private val _uiEvent = Channel<CreateANewListScreenUIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val newListTitle = MutableStateFlow("")
    val newListDescription = MutableStateFlow("")
    val isAPublicList = MutableStateFlow(true)

    val isANumberedList = MutableStateFlow(true)

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
        currentMusicContentSelection.clear()
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
            combine(snapshotFlow { currentMusicContentSelection.toList() }) {
                it
            }.collectLatest {
                if (currentMusicContentSelection.toList()
                        .isNotEmpty() && currentModifyingList.localId == (-1).toLong()
                ) {
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
                snapshotFlow { currentMusicContentSelection.toList() }
            ) { title, desc, isAPublicList, isANumberedList, musicContent ->
                listData.listTitle = title
                listData.listDesc = desc
                listData.isNumberedList = isANumberedList
                listData.isAPublicList = isAPublicList
                listData.currentMusicSelection = musicContent
            }.collectLatest {
                if (currentModifyingList.localId != (-1).toLong() && currentMusicContentSelection.toList()
                        .isNotEmpty()
                ) {
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
                    musicContent = currentMusicContentSelection.toList().map {
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
            loadAnExistingLatestLocalDraftList()
        }
    }

    fun loadASpecificExistingLocalListDraft(listId: Long, onCompletion: () -> Unit) {
        viewModelScope.launch {
            currentModifyingList = listRepo.getThisSpecificLocalList(listId)
            newListTitle.value = currentModifyingList.nameOfTheList
            newListDescription.value = currentModifyingList.descriptionOfTheList
            isAPublicList.value = currentModifyingList.isAPublicList
            isANumberedList.value = currentModifyingList.isANumberedList
            currentMusicContentSelection.clear()
            currentMusicContentSelection.addAll(currentModifyingList.musicContent.map {
                AlbumDetailScreenState(
                    covertArtImgUrl = emptyFlow(),
                    albumImgUrl = it.itemImgUrl,
                    albumTitle = it.itemName,
                    artists = emptyList(),
                    albumWiki = emptyFlow(),
                    releaseDate = "",
                    trackList = emptyFlow(),
                    itemType = "",
                    itemUri = it.itemSpotifyUri
                )
            })
        }.invokeOnCompletion {
            onCompletion()
        }
    }

    private fun loadAnExistingLatestLocalDraftList() {
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
            val userToken = userRepo.getUserData().userToken
            when (musicBoxdAPIRepo.postANewList(
                localListId = currentModifyingList.localId,
                ListDTO(listName = listName,
                    lisDescription = listDescription,
                    isListPublic = isListPublic,
                    spotifyURIs = currentMusicContentSelection.toList()
                        .map { it.itemUri }),
                userToken
            )) {
                is APIResult.Failure -> {
                    pushUiEvent(CreateANewListScreenUIEvent.ShowToast("Failure"))
                }

                is APIResult.Success -> {
                    pushUiEvent(CreateANewListScreenUIEvent.ShowToast("Success"))
                    pushUiEvent(CreateANewListScreenUIEvent.NavigateBack)
                }
            }
        }
    }

    private suspend fun pushUiEvent(createANewListScreenUIEvent: CreateANewListScreenUIEvent) {
        _uiEvent.send(createANewListScreenUIEvent)
    }
}

