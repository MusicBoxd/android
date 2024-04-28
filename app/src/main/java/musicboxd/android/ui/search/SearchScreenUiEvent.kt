package musicboxd.android.ui.search

sealed class SearchScreenUiEvent {
    data class OnQueryChange(val query: String) : SearchScreenUiEvent()
}