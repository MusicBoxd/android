package musicboxd.android.ui.lists

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CreateANewListScreenViewModel : ViewModel() {
    val currentSelection = mutableStateOf(emptyList<String>())
}