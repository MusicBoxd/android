package musicboxd.android.data.remote.api.spotify.model.charts

import kotlinx.serialization.Serializable

@Serializable

data class ChartMetadata(
    val alias: String,
    val backgroundColor: String,
    val dimensions: Dimensions,
    val entityType: String,
    val readableTitle: String,
    val textColor: String,
    val uri: String
)