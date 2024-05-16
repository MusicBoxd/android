package musicboxd.android.data.local.model.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import musicboxd.android.data.local.model.release.Release
import musicboxd.android.data.local.model.review.Review

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Long = 0,
    val userProfileName: String,
    val userName: String,
    val userBio: String,
    val userWebsite: String,
    val profilePicBase64Data: String,
    val profileHeaderBase64Data: String,
    val userLocation: String,
    val userJoinedDate: String,
    val followingCount: Long,
    val followersCount: Long,
    val reviewsCount: Long,
    val reviews: List<Review>,
    val recommendations: List<Release>,
    val likedReleases: List<Release>,
    val listenedReleases: List<Release>,
    val optedInNotifications: List<String>,
    val accountStatus: String,
    val lastActive: String
)