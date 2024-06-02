package musicboxd.android.data.local.list

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import musicboxd.android.data.local.list.model.List


typealias ListOfLocalList = kotlin.collections.List<List>

@Dao
interface ListDao {
    @Query("SELECT * FROM list")
    fun getAllLocalListsAsFlow(): Flow<ListOfLocalList>

    @Insert
    suspend fun createANewLocalList(list: List)

    @Update
    suspend fun updateAnExistingLocalList(list: List)

    @Query("SELECT * FROM list WHERE localId = :localId")
    suspend fun getThisSpecificLocalList(localId: Long): List

    @Query("SELECT * FROM list WHERE localId = :localId")
    fun getThisSpecificLocalListAsFlow(localId: Long): Flow<List>

    @Query("SELECT EXISTS(SELECT 1 FROM list WHERE localId = :localId)")
    fun doesThisListExists(localId: Long): Boolean

    @Query("SELECT * FROM list ORDER BY localId DESC LIMIT 1")
    fun getLatestList(): Flow<List>

}
