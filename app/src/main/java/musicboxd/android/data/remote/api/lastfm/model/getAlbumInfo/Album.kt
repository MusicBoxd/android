package musicboxd.android.data.remote.api.lastfm.model.getAlbumInfo

import kotlinx.serialization.Serializable

@Serializable

data class Album(
    val artist: String,
    val listeners: String,
    val mbid: String,
    val name: String,
    val playcount: String,
    val url: String,
    val wiki: Wiki = Wiki(content = "", published = "", summary = "")
)