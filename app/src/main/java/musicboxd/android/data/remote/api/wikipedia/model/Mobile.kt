package musicboxd.android.data.remote.api.wikipedia.model

import kotlinx.serialization.Serializable

@Serializable

data class Mobile(
    val edit: String,
    val page: String,
    val revisions: String,
    val talk: String
)