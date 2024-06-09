package musicboxd.android.data.local.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT EXISTS (SELECT 1 FROM user WHERE userRemoteId >= 0 LIMIT 1)")
    suspend fun hasAnActiveSession(): Boolean

    @Insert
    suspend fun signup(user: User)

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getUserData(): User

    @Query("SELECT * FROM user LIMIT 1")
    fun getUserDataAsFlow(): Flow<User>
}