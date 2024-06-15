package musicboxd.android.ui.details.artist

import musicboxd.android.data.local.events.model.Event

sealed class ArtistDetailsScreenUiEvent {
    data class SaveAnEvent(val event: Event) : ArtistDetailsScreenUiEvent()
    data class DeleteAnEvent(val eventId: String) : ArtistDetailsScreenUiEvent()
    data class DoesEventExist(val eventId: String) : ArtistDetailsScreenUiEvent()
}