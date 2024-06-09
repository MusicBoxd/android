package musicboxd.android.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import musicboxd.android.data.local.user.UserRepo
import musicboxd.android.data.remote.api.APIResult
import musicboxd.android.data.remote.api.musicboxd.MusicBoxdAPIRepo
import musicboxd.android.data.remote.api.musicboxd.model.list.ListDTO
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val musicBoxdAPIRepo: MusicBoxdAPIRepo,
    private val userRepo: UserRepo
) :
    ViewModel() {
    private val _publicLists = MutableStateFlow(listOf<ListDTO>())
    val publicLists = _publicLists.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            val localUserData = userRepo.getUserData()
            when (val listsData =
                musicBoxdAPIRepo.getPublicLists(localUserData.userToken)) {
                is APIResult.Failure -> {
                    Log.d("10MinMail", listsData.message)
                }

                is APIResult.Success -> {
                    _publicLists.emit(listsData.data.reversed())
                }
            }
        }
    }
}