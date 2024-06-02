package musicboxd.android.data.local.review.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import musicboxd.android.data.remote.api.spotify.model.tracklist.Artist

@Entity(
    tableName = "review"
)
data class Review(
    @PrimaryKey(autoGenerate = true)
    val localReviewId: Long = 0,
    val remoteReviewId: Long,
    val releaseType: String,
    val releaseName: String,
    val releaseImgUrl: String,
    val artists: List<Artist>,
    val spotifyUri: String,
    val isExplicit: Boolean,
    val reviewContent: String,
    val isLiked: Boolean,
    val isRecommended: Boolean,
    val gotAddedIntoAnyLists: Boolean,
    val listIds: List<Long>?,
    val reviewedByUserID: Long,
    val reviewedOnDate: String,
    val reviewTitle: String,
    val rating: Float,
    val timeStamp: Long,
    val genres: List<String>,
    val noOfLikesForThisReview: Long,
    val reviewTags: List<String>
)