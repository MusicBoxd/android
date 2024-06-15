package musicboxd.android.data.local.events

import kotlinx.coroutines.flow.Flow
import musicboxd.android.data.local.events.model.Event
import javax.inject.Inject

class EventImpl @Inject constructor(private val eventDao: EventDao) : EventRepo {
    override suspend fun saveANewEvent(event: Event) {
        eventDao.saveANewEvent(event)
    }

    override suspend fun deleteAnExistingEvent(eventUri: String) {
        eventDao.deleteAnExistingEvent(eventUri)
    }

    override fun doesEventExist(eventUri: String): Flow<Boolean> {
        return eventDao.doesEventExist(eventUri)
    }
}