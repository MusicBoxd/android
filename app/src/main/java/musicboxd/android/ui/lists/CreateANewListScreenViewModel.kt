package musicboxd.android.ui.lists

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import musicboxd.android.ui.details.album.AlbumDetailScreenState

class CreateANewListScreenViewModel : ViewModel() {
    val currentSelection = mutableStateOf(emptyList<AlbumDetailScreenState?>())
}