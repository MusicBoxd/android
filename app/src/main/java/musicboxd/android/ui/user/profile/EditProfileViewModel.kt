package musicboxd.android.ui.user.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import musicboxd.android.ui.user.profile.model.EditProfileState

class EditProfileViewModel : ViewModel() {
    private val profileName = mutableStateOf("")
    private val profileUsername = mutableStateOf("")
    private val profileBio = mutableStateOf("")
    private val profileLocation = mutableStateOf("")
    private val profileWebsite = mutableStateOf("")
    val editProfileState = listOf(
        EditProfileState("Name", profileName),
        EditProfileState("Username", profileUsername),
        EditProfileState("Bio", profileBio),
        EditProfileState("Location", profileLocation),
        EditProfileState("Website", profileWebsite),
    )
}