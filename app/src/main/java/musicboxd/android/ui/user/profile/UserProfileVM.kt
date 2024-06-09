package musicboxd.android.ui.user.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import musicboxd.android.data.local.user.User
import musicboxd.android.data.local.user.UserRepo
import javax.inject.Inject

@HiltViewModel
class UserProfileVM @Inject constructor(private val userRepo: UserRepo) : ViewModel() {
    private val _userData = MutableStateFlow(
        User(
            userRemoteId = -1,
            userProfileName = "",
            password = "",
            userToken = "",
            refreshToken = "",
            userName = "",
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

    val userData = _userData.asStateFlow()

    init {
        viewModelScope.launch {
            userRepo.getUserDataAsFlow().collectLatest {
                _userData.emit(it)
            }
        }
    }
}