package musicboxd.android.data.remote.api.spotify.charts.model

import kotlinx.serialization.Serializable

@Serializable

data class ChartEntryData(
    val appearancesOnChart: Int,
    val consecutiveAppearancesOnChart: Int,
    val currentRank: Int,
    val entryDate: String,
    val entryRank: Int,
    val entryStatus: String,
    val peakDate: String,
    val peakRank: Int,
    val previousRank: Int
)