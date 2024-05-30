package musicboxd.android.data.local.review

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import musicboxd.android.data.local.model.review.Review

@Dao
interface ReviewDao {
    @Insert
    suspend fun createANewLocalReview(review: Review)

    @Update
    suspend fun updateAnExistingLocalReview(review: Review)

    @Query("SELECT EXISTS(SELECT 1 FROM review WHERE spotifyUri = :spotifyUri)")
    suspend fun doesThisReviewExistsOnLocalDevice(spotifyUri: String): Boolean


    @Query("SELECT * FROM review ORDER BY localReviewId DESC LIMIT 1")
    suspend fun getTheLatestAddedLocalReview(): Review

    @Query("SELECT * FROM review ORDER BY localReviewId DESC LIMIT 1")
    fun getTheLatestAddedLocalReviewAsFlow(): Flow<Review>

    @Query("SELECT * FROM review WHERE spotifyUri = :spotifyUri")
    suspend fun getASpecificLocalReview(spotifyUri: String): Review

    @Query("SELECT * FROM review WHERE spotifyUri = :spotifyUri")
    fun getASpecificLocalReviewAsFlow(spotifyUri: String): Flow<Review>

    @Query("SELECT * FROM review")
    fun getAllExistingLocalReviews(): Flow<List<Review>>
}