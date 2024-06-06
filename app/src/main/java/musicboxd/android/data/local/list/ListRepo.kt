package musicboxd.android.data.local.list

import kotlinx.coroutines.flow.Flow
import musicboxd.android.data.local.list.model.List

interface ListRepo {
    fun getAllLocalListsAsFlow(): Flow<ListOfLocalList>
    suspend fun createANewLocalList(list: List)
    suspend fun updateAnExistingLocalList(list: List)
    suspend fun getThisSpecificLocalList(localId: Long): List
    fun getThisSpecificLocalListAsFlow(localId: Long): Flow<List>
    fun doesThisListExists(localId: Long): Boolean
    fun getLatestList(): Flow<List>
    suspend fun deleteAnExistingLocalList(localId: Long)

}