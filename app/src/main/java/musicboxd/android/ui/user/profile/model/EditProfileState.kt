package musicboxd.android.ui.user.profile.model

import androidx.compose.runtime.MutableState

data class EditProfileState(
    val editItemName: String,
    val editItemValue: MutableState<String>
)
