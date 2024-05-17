package musicboxd.android.data.local.typeconverters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import musicboxd.android.data.remote.api.spotify.model.tracklist.Artist

class ReleaseToArtistTypeConverter {
    private val json = Json

    @TypeConverter
    fun listOfArtistToString(artistList: List<Artist>): String {
        return json.encodeToString(artistList)
    }

    @TypeConverter
    fun stringToListOfArtist(str: String): List<Artist> {
        return json.decodeFromString<List<Artist>>(str)
    }
}