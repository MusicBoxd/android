package musicboxd.android.data.local.events.typeconverters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import musicboxd.android.data.remote.scrape.artist.tour.model.event.EventDetailsDTO

class EventDetailsDTOTypeConverter {
    private val json = Json

    @TypeConverter
    fun fromString(value: String): EventDetailsDTO {
        return json.decodeFromString(value)
    }

    @TypeConverter
    fun fromItem(item: EventDetailsDTO): String {
        return json.encodeToString(item)
    }
}