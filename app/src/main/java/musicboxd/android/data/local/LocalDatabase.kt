package musicboxd.android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import musicboxd.android.data.local.model.release.Release
import musicboxd.android.data.local.model.review.Review
import musicboxd.android.data.local.model.user.User

@Database(version = 1, entities = [User::class, Release::class, Review::class])
abstract class LocalDatabase : RoomDatabase() {

}