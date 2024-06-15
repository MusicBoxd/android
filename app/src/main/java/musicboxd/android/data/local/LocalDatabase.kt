package musicboxd.android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import musicboxd.android.data.local.events.EventDao
import musicboxd.android.data.local.events.model.Event
import musicboxd.android.data.local.events.typeconverters.ArtistTourDTOTypeConverter
import musicboxd.android.data.local.events.typeconverters.EventDetailsDTOTypeConverter
import musicboxd.android.data.local.list.ListDao
import musicboxd.android.data.local.list.model.List
import musicboxd.android.data.local.release.Release
import musicboxd.android.data.local.review.ReviewDao
import musicboxd.android.data.local.review.model.Review
import musicboxd.android.data.local.typeconverters.ListOfMusicContentTypeConverter
import musicboxd.android.data.local.typeconverters.LongListTypeConverter
import musicboxd.android.data.local.typeconverters.ReleaseToArtistTypeConverter
import musicboxd.android.data.local.typeconverters.StringListTypeConverter
import musicboxd.android.data.local.user.User
import musicboxd.android.data.local.user.UserDao

@Database(
    version = 1,
    entities = [Event::class, User::class, Release::class, Review::class, List::class]
)
@TypeConverters(
    LongListTypeConverter::class,
    ReleaseToArtistTypeConverter::class,
    StringListTypeConverter::class,
    ListOfMusicContentTypeConverter::class,
    ArtistTourDTOTypeConverter::class,
    EventDetailsDTOTypeConverter::class,
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun reviewDao(): ReviewDao
    abstract fun localListDao(): ListDao

    abstract fun userDao(): UserDao
    abstract fun eventDao(): EventDao
}