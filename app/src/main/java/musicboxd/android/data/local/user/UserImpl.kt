package musicboxd.android.data.local.user

import kotlinx.coroutines.flow.Flow
import musicboxd.android.data.local.LocalDatabase
import javax.inject.Inject

class UserImpl @Inject constructor(private val localDatabase: LocalDatabase) : UserRepo {
    override suspend fun hasAnActiveSession(): Boolean {
        return localDatabase.userDao().hasAnActiveSession()
    }

    override suspend fun signup(user: User) {
        localDatabase.userDao().signup(user)
    }

    override suspend fun getUserData(): User {
        return localDatabase.userDao().getUserData()
    }

    override suspend fun getUserDataAsFlow(): Flow<User> {
        return localDatabase.userDao().getUserDataAsFlow()
    }
}