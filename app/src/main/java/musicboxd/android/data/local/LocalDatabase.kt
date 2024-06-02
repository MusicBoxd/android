package musicboxd.android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import musicboxd.android.data.local.list.ListDao
import musicboxd.android.data.local.list.model.List
import musicboxd.android.data.local.model.release.Release
import musicboxd.android.data.local.model.user.User
import musicboxd.android.data.local.review.ReviewDao
import musicboxd.android.data.local.review.model.Review
import musicboxd.android.data.local.typeconverters.ListOfMusicContentTypeConverter
import musicboxd.android.data.local.typeconverters.LongListTypeConverter
import musicboxd.android.data.local.typeconverters.ReleaseToArtistTypeConverter
import musicboxd.android.data.local.typeconverters.StringListTypeConverter

@Database(version = 1, entities = [User::class, Release::class, Review::class, List::class])
@TypeConverters(
    LongListTypeConverter::class,
    ReleaseToArtistTypeConverter::class,
    StringListTypeConverter::class,
    ListOfMusicContentTypeConverter::class
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun reviewDao(): ReviewDao
    abstract fun localListDao(): ListDao
}