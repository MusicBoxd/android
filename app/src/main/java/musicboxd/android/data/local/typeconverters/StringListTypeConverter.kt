package musicboxd.android.data.local.typeconverters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class StringListTypeConverter {
    private val json = Json

    @TypeConverter
    fun stringListToString(strList: List<String>): String {
        return json.encodeToString(strList)
    }

    @TypeConverter
    fun stringToListOfString(str: String): List<String> {
        return json.decodeFromString<List<String>>(str)
    }
}