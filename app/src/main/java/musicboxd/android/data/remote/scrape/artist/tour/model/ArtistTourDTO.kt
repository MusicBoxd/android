package musicboxd.android.data.remote.scrape.artist.tour.model

import kotlinx.serialization.Serializable

@Serializable
data class ArtistTourDTO(
    val location: String,
    val venue: String,
    val day: String,
    val date: String,
    val time: String,
    val href: String
)
