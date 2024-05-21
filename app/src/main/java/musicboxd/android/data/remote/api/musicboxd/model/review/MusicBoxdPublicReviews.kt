package musicboxd.android.data.remote.api.musicboxd.model.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MusicBoxdPublicReviews(
    val albumId: String,
    @SerialName("reviewText")
    val albumReview: String,
    @SerialName("rating")
    val albumRating: Float
)
