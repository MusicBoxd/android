package musicboxd.android.data.local.typeconverters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import musicboxd.android.data.local.list.model.MusicContent

class ListOfMusicContentTypeConverter {

    private val json = Json

    @TypeConverter
    fun musicContentListToString(musicContentList: List<MusicContent>): String {
        return json.encodeToString(musicContentList)
    }

    @TypeConverter
    fun stringToListOfMusicContent(str: String): List<MusicContent> {
        return json.decodeFromString<List<MusicContent>>(str)
    }
}