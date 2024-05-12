package musicboxd.android.data.remote.api.spotify.charts.model

import kotlinx.serialization.Serializable

@Serializable

data class Highlight(
    val backgroundColor: String,
    val chartLabel: String,
    val chartUri: String,
    val dateLabel: String,
    val displayImageUri: String,
    val text: String,
    val type: String
)