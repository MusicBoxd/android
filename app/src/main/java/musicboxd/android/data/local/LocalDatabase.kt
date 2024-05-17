package musicboxd.android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import musicboxd.android.data.local.model.release.Release
import musicboxd.android.data.local.model.review.Review
import musicboxd.android.data.local.model.user.User
import musicboxd.android.data.local.typeconverters.LongListTypeConverter
import musicboxd.android.data.local.typeconverters.ReleaseToArtistTypeConverter
import musicboxd.android.data.local.typeconverters.StringListTypeConverter

@Database(version = 1, entities = [User::class, Release::class, Review::class])
@TypeConverters(
    LongListTypeConverter::class,
    ReleaseToArtistTypeConverter::class,
    StringListTypeConverter::class
)
abstract class LocalDatabase : RoomDatabase() {

}