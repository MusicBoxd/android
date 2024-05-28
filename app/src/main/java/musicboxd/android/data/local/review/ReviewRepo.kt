package musicboxd.android.data.local.review

import kotlinx.coroutines.flow.Flow
import musicboxd.android.data.local.model.review.Review

interface ReviewRepo {
    suspend fun createANewLocalReview(review: Review)
    suspend fun updateAnExistingLocalReview(review: Review)
    suspend fun doesThisReviewExistsOnLocalDevice(spotifyUri: String): Boolean
    suspend fun getTheLatestAddedLocalReview(): Review
    suspend fun getASpecificLocalReview(spotifyUri: String): Review

    fun getAllExistingLocalReviews(): Flow<List<Review>>
}