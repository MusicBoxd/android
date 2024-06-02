package musicboxd.android.data.local.list.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.collections.List


@Entity("list")
data class List(
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0,
    val remoteId: Long,
    val nameOfTheList: String,
    val descriptionOfTheList: String,
    val isAPublicList: Boolean,
    val isANumberedList: Boolean,
    val musicContent: List<MusicContent>,
    val dateCreated: String,
    val timeStamp: String,
    val noOfLikes: Long
)
