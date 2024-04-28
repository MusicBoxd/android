package musicboxd.android.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import musicboxd.android.data.remote.api.lastfm.LastFMAPIImpl
import musicboxd.android.data.remote.api.lastfm.LastFMAPIRepo
import musicboxd.android.data.remote.api.lastfm.LastFMAPIService
import musicboxd.android.data.remote.api.musicbrainz.MusicBrainzAPIImpl
import musicboxd.android.data.remote.api.musicbrainz.MusicBrainzAPIRepo
import musicboxd.android.data.remote.api.musicbrainz.MusicBrainzAPIService
import musicboxd.android.data.remote.api.spotify.SpotifyAPIImpl
import musicboxd.android.data.remote.api.spotify.SpotifyAPIRepo
import musicboxd.android.data.remote.api.spotify.SpotifyAPIService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideLastFmAPIInstance(): LastFMAPIService {
        return Retrofit.Builder().baseUrl("https://ws.audioscrobbler.com/").addConverterFactory(
            json.asConverterFactory(
                "application/json".toMediaType()
            )
        )
            .build().create(LastFMAPIService::class.java)
    }

    @Provides
    @Singleton
    fun provideMusicBrainzAPIInstance(): MusicBrainzAPIService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header(
                        "User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
                    )
                    .build()
                chain.proceed(request)
            }
            .build()
        return Retrofit.Builder().baseUrl("https://musicbrainz.org/ws/2/").client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType())).build()
            .create(MusicBrainzAPIService::class.java)
    }

    @Provides
    @Singleton
    fun provideMusicBrainzAPIRepo(musicBrainzAPIService: MusicBrainzAPIService): MusicBrainzAPIRepo {
        return MusicBrainzAPIImpl(musicBrainzAPIService)
    }

    @Provides
    @Singleton
    fun provideSpotifyAPIInstance(): SpotifyAPIService {
        return Retrofit.Builder().baseUrl("https://api.spotify.com/v1/").addConverterFactory(
            json.asConverterFactory(
                "application/json".toMediaType()
            )
        )
            .build().create(SpotifyAPIService::class.java)
    }

    @Provides
    @Singleton
    fun provideSpotifyAPIRepo(spotifyAPIService: SpotifyAPIService): SpotifyAPIRepo {
        return SpotifyAPIImpl(spotifyAPIService)
    }

    @Provides
    @Singleton
    fun provideLastFmRepo(lastFMAPIService: LastFMAPIService): LastFMAPIRepo {
        return LastFMAPIImpl(lastFMAPIService)
    }
}