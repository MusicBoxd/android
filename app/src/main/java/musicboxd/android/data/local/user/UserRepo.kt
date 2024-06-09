package musicboxd.android.data.local.user

import kotlinx.coroutines.flow.Flow

interface UserRepo {
    suspend fun hasAnActiveSession(): Boolean
    suspend fun signup(user: User)
    suspend fun getUserData(): User
    suspend fun getUserDataAsFlow(): Flow<User>

}