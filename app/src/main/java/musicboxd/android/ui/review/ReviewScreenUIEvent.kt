package musicboxd.android.ui.review

sealed class ReviewScreenUIEvent {
    data class ShowToast(val toastMessage: String) : ReviewScreenUIEvent()
    data object Nothing : ReviewScreenUIEvent()
}