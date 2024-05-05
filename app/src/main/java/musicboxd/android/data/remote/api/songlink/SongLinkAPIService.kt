package musicboxd.android.data.remote.api.songlink

import musicboxd.android.data.remote.api.songlink.model.SongLinkAPIDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface SongLinkAPIService {
    @GET("links")
    suspend fun getLinks(@Query("url") spotifyUrl: String): SongLinkAPIDTO
}