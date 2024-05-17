package musicboxd.android.data.local.typeconverters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LongListTypeConverter {
    private val json = Json

    @TypeConverter
    fun longListToString(longList: List<Long>): String {
        return json.encodeToString(longList)
    }

    @TypeConverter
    fun stringToListOfLong(str: String): List<Long> {
        return json.decodeFromString<List<Long>>(str)
    }
}