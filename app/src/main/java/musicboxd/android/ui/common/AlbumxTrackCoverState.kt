package musicboxd.android.ui.common

import musicboxd.android.data.remote.api.spotify.model.album.Artist

data class AlbumxTrackCoverState(
    val covertImgUrl: String,
    val mainImgUrl: String,
    val itemTitle: String,
    val itemType: String,
    val itemArtists: List<Artist>
)
