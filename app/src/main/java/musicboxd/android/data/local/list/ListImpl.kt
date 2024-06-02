package musicboxd.android.data.local.list

import kotlinx.coroutines.flow.Flow
import musicboxd.android.data.local.LocalDatabase
import musicboxd.android.data.local.list.model.List
import javax.inject.Inject

class ListImpl @Inject constructor(private val localDatabase: LocalDatabase) : ListRepo {
    override fun getAllLocalListsAsFlow(): Flow<ListOfLocalList> {
        return localDatabase.localListDao().getAllLocalListsAsFlow()
    }

    override suspend fun createANewLocalList(list: List) {
        return localDatabase.localListDao().createANewLocalList(list)
    }

    override suspend fun updateAnExistingLocalList(list: List) {
        return localDatabase.localListDao().updateAnExistingLocalList(list)
    }

    override suspend fun getThisSpecificLocalList(localId: Long): List {
        return localDatabase.localListDao().getThisSpecificLocalList(localId)
    }

    override fun getThisSpecificLocalListAsFlow(localId: Long): Flow<List> {
        return localDatabase.localListDao().getThisSpecificLocalListAsFlow(localId)
    }

    override fun doesThisListExists(localId: Long): Boolean {
        return localDatabase.localListDao().doesThisListExists(localId)
    }

    override fun getLatestList(): Flow<List> {
        return localDatabase.localListDao().getLatestList()
    }
}