package musicboxd.android.data.local.events

import kotlinx.coroutines.flow.Flow
import musicboxd.android.data.local.events.model.Event

interface EventRepo {
    suspend fun saveANewEvent(event: Event)
    suspend fun deleteAnExistingEvent(eventUri: String)
    fun doesEventExist(eventUri: String): Flow<Boolean>
}