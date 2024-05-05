package musicboxd.android.data.remote.api.songlink

import musicboxd.android.data.remote.api.songlink.model.SongLinkAPIDTO

interface SongLinkRepo {
    suspend fun getLinks(spotifyUrl: String): SongLinkAPIDTO
}