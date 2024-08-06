package musicboxd.android.ui.startup.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import musicboxd.android.data.local.user.User
import musicboxd.android.data.local.user.UserRepo
import musicboxd.android.data.remote.api.APIResult
import musicboxd.android.data.remote.api.musicboxd.MusicBoxdAPIRepo
import musicboxd.android.data.remote.api.musicboxd.model.MusicBoxdLoginDTO
import musicboxd.android.ui.navigation.NavigationRoutes
import musicboxd.android.ui.startup.StartUpScreenEvent
import javax.inject.Inject

@HiltViewModel
class SignInVM @Inject constructor(
    private val userRepo: UserRepo,
    private val musicBoxdAPIRepo: MusicBoxdAPIRepo
) : ViewModel() {
    private val _uiEvent = Channel<StartUpScreenEvent>()
    val uiEvent = _uiEvent
    val uiEventAsFlow = _uiEvent.receiveAsFlow()

    private val _showProgressBarInBottomBar = MutableStateFlow(false)
    val showProgressBarInBottomBar = _showProgressBarInBottomBar.asStateFlow()

    fun signIn(username: String, password: String) {
        viewModelScope.launch {
            _showProgressBarInBottomBar.emit(true)
            val userLoginData = musicBoxdAPIRepo.getUserToken(MusicBoxdLoginDTO(username, password))
            when (userLoginData) {
                is APIResult.Failure -> {
                    println(userLoginData.message)
                }
                is APIResult.Success -> {
                    userRepo.signup(
                        User(
                            userRemoteId = userLoginData.data.userId,
                            userProfileName = "null",
                            password = password,
                            userToken = userLoginData.data.jwt,
                            refreshToken = userLoginData.data.refreshJwt,
                            userName = userLoginData.data.username,
                            userBio = "",
                            userWebsite = "",
                            profilePicBase64Data = "",
                            profileHeaderBase64Data = "",
                            userLocation = "",
                            userJoinedDate = "",
                            followingCount = 0L,
                            followersCount = 0L,
                            reviewsCount = 0L,
                            reviews = listOf(),
                            recommendations = listOf(),
                            likedReleases = listOf(),
                            listenedReleases = listOf(),
                            optedInNotifications = listOf(),
                            accountStatus = "",
                            lastActive = "",
                            savedEvents = listOf()
                        )
                    )
                }
            }
            _showProgressBarInBottomBar.emit(false)
        }.invokeOnCompletion {
            sendUIEvent(StartUpScreenEvent.Navigate(NavigationRoutes.HOME.name))
        }
    }

    private fun sendUIEvent(uiEvent: StartUpScreenEvent) {
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }
}