package musicboxd.android.ui.lists

sealed class CreateANewListScreenUIEvent {
    data class ShowToast(val msg: String) : CreateANewListScreenUIEvent()
    data object NavigateBack : CreateANewListScreenUIEvent()
    data object Nothing : CreateANewListScreenUIEvent()
}