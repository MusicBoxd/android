package musicboxd.android.data.local.review

import kotlinx.coroutines.flow.Flow
import musicboxd.android.data.local.LocalDatabase
import musicboxd.android.data.local.model.review.Review
import javax.inject.Inject

class ReviewImpl @Inject constructor(private val localDatabase: LocalDatabase) : ReviewRepo {
    override suspend fun createANewLocalReview(review: Review) {
        localDatabase.reviewDao().createANewLocalReview(review)
    }

    override suspend fun updateAnExistingLocalReview(review: Review) {
        localDatabase.reviewDao().updateAnExistingLocalReview(review)
    }

    override suspend fun doesThisReviewExistsOnLocalDevice(spotifyUri: String): Boolean {
        return localDatabase.reviewDao().doesThisReviewExistsOnLocalDevice(spotifyUri)
    }

    override suspend fun getTheLatestAddedLocalReview(): Review {
        return localDatabase.reviewDao().getTheLatestAddedLocalReview()
    }

    override suspend fun getTheLatestAddedLocalReviewAsFlow(): Flow<Review> {
        return localDatabase.reviewDao().getTheLatestAddedLocalReviewAsFlow()
    }

    override suspend fun getASpecificLocalReviewAsFlow(spotifyUri: String): Flow<Review> {
        return localDatabase.reviewDao().getASpecificLocalReviewAsFlow(spotifyUri)
    }

    override suspend fun getASpecificLocalReview(spotifyUri: String): Review {
        return localDatabase.reviewDao().getASpecificLocalReview(spotifyUri)
    }

    override fun getAllExistingLocalReviews(): Flow<List<Review>> {
        return localDatabase.reviewDao().getAllExistingLocalReviews()
    }
}