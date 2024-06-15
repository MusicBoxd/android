package musicboxd.android.data.local.events.typeconverters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import musicboxd.android.data.remote.scrape.artist.tour.model.ArtistTourDTO

class ArtistTourDTOTypeConverter {
    private val json = Json

    @TypeConverter
    fun fromString(value: String): ArtistTourDTO {
        return json.decodeFromString(value)
    }

    @TypeConverter
    fun fromItem(artistTourDTO: ArtistTourDTO): String {
        return json.encodeToString(artistTourDTO)
    }
}