package musicboxd.android.data.remote.api.lastfm.model.searchAlbums

import kotlinx.serialization.Serializable

@Serializable
data class SearchAlbumsFromLastFm(
    val results: Results
)