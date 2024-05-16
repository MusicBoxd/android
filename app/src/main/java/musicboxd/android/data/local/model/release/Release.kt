package musicboxd.android.data.local.model.release

import androidx.room.Entity
import androidx.room.PrimaryKey
import musicboxd.android.data.remote.api.spotify.model.tracklist.Artist

@Entity(tableName = "release")
data class Release(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0,
    val releaseType: String,
    val releaseName: String,
    val artists: List<Artist>,
    val imageUrl: String,
    val spotifyUri: String,
    val isExplicit: Boolean
)
