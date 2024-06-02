package musicboxd.android.data.remote.api.musicboxd.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewDTO(
    @SerialName("reviewText")
    val reviewContent: String,
    val itemUri: String,
    @SerialName("rating")
    val reviewRating: Float,
    val reviewTitle: String
)