package musicboxd.android.data.remote.api.spotify.model.charts

import kotlinx.serialization.Serializable

@Serializable

data class Dimensions(
    val chartType: String,
    val city: String,
    val country: String,
    val earliestDate: String,
    val genre: String,
    val latestDate: String,
    val recurrence: String
)