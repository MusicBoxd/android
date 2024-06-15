package musicboxd.android.data.local.events

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import musicboxd.android.data.local.events.model.Event

@Dao
interface EventDao {
    @Insert
    suspend fun saveANewEvent(event: Event)

    @Query("DELETE FROM event WHERE eventId = :eventUri")
    suspend fun deleteAnExistingEvent(eventUri: String)

    @Query("SELECT EXISTS(SELECT * FROM event WHERE eventId = :eventUri)")
    fun doesEventExist(eventUri: String): Flow<Boolean>
}