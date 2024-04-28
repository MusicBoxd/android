package musicboxd.android.data.remote.api.lastfm.model.searchArtists

import kotlinx.serialization.Serializable

@Serializable
data class SearchArtists(
    val results: Results
)