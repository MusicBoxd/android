package musicboxd.android.data.remote.api.lastfm.model.searchTracks

import kotlinx.serialization.Serializable

@Serializable
data class SearchTracks(
    val results: Results
)