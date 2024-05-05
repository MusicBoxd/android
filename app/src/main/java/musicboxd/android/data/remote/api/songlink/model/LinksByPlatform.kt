package musicboxd.android.data.remote.api.songlink.model

import kotlinx.serialization.Serializable

@Serializable
data class LinksByPlatform(
    val amazonMusic: Link = Link(url = ""),
    val anghami: Link = Link(url = ""),
    val appleMusic: Link = Link(url = ""),
    val audiomack: Link = Link(url = ""),
    val boomplay: Link = Link(url = ""),
    val deezer: Link = Link(url = ""),
    val itunes: Link = Link(url = ""),
    val napster: Link = Link(url = ""),
    val pandora: Link = Link(url = ""),
    val soundcloud: Link = Link(url = ""),
    val spotify: Link = Link(url = ""),
    val tidal: Link = Link(url = ""),
    val yandex: Link = Link(url = ""),
    val youtube: Link = Link(url = ""),
    val youtubeMusic: Link = Link(url = "")
)