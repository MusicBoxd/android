package musicboxd.android.ui.startup

import musicboxd.android.data.remote.api.musicboxd.model.user.SignUpDTO

sealed class StartUpScreenEvent {
    data object FetchingToken : StartUpScreenEvent()
    data object FetchingAccountData : StartUpScreenEvent()
    data object CheckingIfAnySessionAlreadyExists : StartUpScreenEvent()
    data object AddingDataToLocalDatabase : StartUpScreenEvent()
    data class Navigate(val navigationRoute: String) : StartUpScreenEvent()
    data class SignUp(val signUpDTO: SignUpDTO) : StartUpScreenEvent()
    data object None : StartUpScreenEvent()
}