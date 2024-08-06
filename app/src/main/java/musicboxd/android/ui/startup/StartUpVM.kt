package musicboxd.android.ui.startup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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
import musicboxd.android.data.remote.api.musicboxd.model.user.SignUpDTO
import musicboxd.android.ui.navigation.NavigationRoutes
import javax.inject.Inject

@HiltViewModel
class StartUpVM @Inject constructor(
    private val userRepo: UserRepo,
    private val musicBoxdAPIRepo: MusicBoxdAPIRepo
) : ViewModel() {
    private val _uiEvent = Channel<StartUpScreenEvent>()
    val uiEvent = _uiEvent
    val uiEventAsFlow = _uiEvent.receiveAsFlow()

    private val _showProgressBarInBottomBar = MutableStateFlow(false)
    val showProgressBarInBottomBar = _showProgressBarInBottomBar.asStateFlow()

    fun fromUIEvent(uiEvent: StartUpScreenEvent) {
        when (uiEvent) {
            is StartUpScreenEvent.SignUp -> {
                viewModelScope.launch {
                    _showProgressBarInBottomBar.emit(true)
                    val signupData = async {
                        musicBoxdAPIRepo.createANewUser(
                            SignUpDTO(
                                userProfileName = uiEvent.signUpDTO.userProfileName,
                                username = uiEvent.signUpDTO.username,
                                password = uiEvent.signUpDTO.password
                            )
                        )
                    }.await()
                    when (signupData) {
                        is APIResult.Failure -> {
                            println(signupData.message)
                        }
                        is APIResult.Success -> {
                            val userToken = musicBoxdAPIRepo.getUserToken(
                                MusicBoxdLoginDTO(
                                    userName = uiEvent.signUpDTO.username,
                                    password = uiEvent.signUpDTO.password
                                )
                            )
                            when (userToken) {
                                is APIResult.Failure -> TODO()
                                is APIResult.Success -> {
                                    userRepo.signup(
                                        User(
                                            userRemoteId = signupData.data.remoteId,
                                            userProfileName = uiEvent.signUpDTO.userProfileName,
                                            password = uiEvent.signUpDTO.password,
                                            userName = uiEvent.signUpDTO.username,
                                            userBio = "",
                                            userToken = userToken.data.jwt,
                                            refreshToken = userToken.data.refreshJwt,
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
                        }
                    }
                    _showProgressBarInBottomBar.emit(false)
                }.invokeOnCompletion {
                    sendUIEvent(StartUpScreenEvent.Navigate(NavigationRoutes.HOME.name))
                }
            }

            else -> {}
        }
    }

    init {
        viewModelScope.launch {
            sendUIEvent(StartUpScreenEvent.CheckingIfAnySessionAlreadyExists)
            if (userRepo.hasAnActiveSession()) {
                return@launch sendUIEvent(StartUpScreenEvent.Navigate(NavigationRoutes.HOME.name))
            }
            sendUIEvent(StartUpScreenEvent.None)
        }
    }

    private fun sendUIEvent(uiEvent: StartUpScreenEvent) {
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }
}