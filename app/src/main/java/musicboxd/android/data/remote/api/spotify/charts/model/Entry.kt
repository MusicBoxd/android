package musicboxd.android.data.remote.api.spotify.charts.model

import kotlinx.serialization.Serializable

@Serializable

data class Entry(
    val albumMetadata: AlbumMetadata = AlbumMetadata(
        albumName = "",
        albumUri = "",
        artists = listOf(),
        displayImageUri = "",
        releaseDate = ""
    ),
    val artistMetadata: ArtistMetadata = ArtistMetadata(
        artistName = "",
        artistUri = "",
        displayImageUri = ""
    ),
    val chartEntryData: ChartEntryData = ChartEntryData(
        appearancesOnChart = 0,
        consecutiveAppearancesOnChart = 0,
        currentRank = 0,
        entryDate = "",
        entryRank = 0,
        entryStatus = "",
        peakDate = "",
        peakRank = 0,
        previousRank = 0
    ),
    val missingRequiredFields: Boolean,
    val trackMetadata: TrackMetadata = TrackMetadata(
        artists = listOf(),
        displayImageUri = "",
        releaseDate = "",
        trackName = "",
        trackUri = ""
    )
)