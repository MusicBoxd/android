package musicboxd.android.di

import android.app.Application
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.nerdythings.okhttp.profiler.OkHttpProfilerInterceptor
import kotlinx.serialization.json.Json
import musicboxd.android.data.local.LocalDatabase
import musicboxd.android.data.local.events.EventDao
import musicboxd.android.data.local.events.EventImpl
import musicboxd.android.data.local.events.EventRepo
import musicboxd.android.data.local.list.ListImpl
import musicboxd.android.data.local.list.ListRepo
import musicboxd.android.data.local.review.ReviewImpl
import musicboxd.android.data.local.review.ReviewRepo
import musicboxd.android.data.local.user.UserImpl
import musicboxd.android.data.local.user.UserRepo
import musicboxd.android.data.remote.api.lastfm.LastFMAPIImpl
import musicboxd.android.data.remote.api.lastfm.LastFMAPIRepo
import musicboxd.android.data.remote.api.lastfm.LastFMAPIService
import musicboxd.android.data.remote.api.musicboxd.MusicBoxdAPIImpl
import musicboxd.android.data.remote.api.musicboxd.MusicBoxdAPIRepo
import musicboxd.android.data.remote.api.musicboxd.MusicBoxdAPIService
import musicboxd.android.data.remote.api.musicbrainz.MusicBrainzAPIImpl
import musicboxd.android.data.remote.api.musicbrainz.MusicBrainzAPIRepo
import musicboxd.android.data.remote.api.musicbrainz.MusicBrainzAPIService
import musicboxd.android.data.remote.api.songlink.SongLinkAPIService
import musicboxd.android.data.remote.api.songlink.SongLinkImpl
import musicboxd.android.data.remote.api.songlink.SongLinkRepo
import musicboxd.android.data.remote.api.spotify.SpotifyAPIImpl
import musicboxd.android.data.remote.api.spotify.SpotifyAPIRepo
import musicboxd.android.data.remote.api.spotify.SpotifyAPIService
import musicboxd.android.data.remote.api.spotify.charts.SpotifyChartsAPIImpl
import musicboxd.android.data.remote.api.spotify.charts.SpotifyChartsAPIRepo
import musicboxd.android.data.remote.api.spotify.charts.SpotifyChartsAPIService
import musicboxd.android.data.remote.scrape.artist.tour.ArtistTourImpl
import musicboxd.android.data.remote.scrape.artist.tour.ArtistTourRepo
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.Body
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
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
    fun provideSongLinkAPIInstance(): SongLinkAPIService {
        return Retrofit.Builder().baseUrl("https://api.song.link/v1-alpha.1/").addConverterFactory(
            json.asConverterFactory(
                "application/json".toMediaType()
            )
        )
            .build().create(SongLinkAPIService::class.java)
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
    fun provideSpotifyChartsAPIInstance(): SpotifyChartsAPIService {
        return Retrofit.Builder()
            .baseUrl("https://charts-spotify-com-service.spotify.com/public/v0/")
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build().create(SpotifyChartsAPIService::class.java)
    }

    @Provides
    @Singleton
    fun provideMusicBoxdAPIInstance(): MusicBoxdAPIService {

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS).addInterceptor(OkHttpProfilerInterceptor())

        return Retrofit.Builder()
            .baseUrl("http://musicbodx.eu-north-1.elasticbeanstalk.com/")
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .client(okHttpClient.build())
            .build().create(MusicBoxdAPIService::class.java)
    }

    @Provides
    @Singleton
    fun provideSpotifyAPIRepo(spotifyAPIService: SpotifyAPIService): SpotifyAPIRepo {
        return SpotifyAPIImpl(spotifyAPIService)
    }

    @Provides
    @Singleton
    fun provideSpotifyChartsAPIRepo(spotifyChartsAPIService: SpotifyChartsAPIService): SpotifyChartsAPIRepo {
        return SpotifyChartsAPIImpl(spotifyChartsAPIService)
    }

    @Provides
    @Singleton
    fun provideLastFmRepo(lastFMAPIService: LastFMAPIService): LastFMAPIRepo {
        return LastFMAPIImpl(lastFMAPIService)
    }

    @Provides
    @Singleton
    fun provideSongLinkRepo(songLinkAPIService: SongLinkAPIService): SongLinkRepo {
        return SongLinkImpl(songLinkAPIService)
    }

    @Provides
    @Singleton
    fun provideMusicBoxdAPIRepo(
        musicBoxdAPIService: MusicBoxdAPIService,
        listImpl: ListImpl
    ): MusicBoxdAPIRepo {
        return MusicBoxdAPIImpl(musicBoxdAPIService, listImpl)
    }

    @Provides
    @Singleton
    fun provideArtistConcertsRepo(): ArtistTourRepo {
        return ArtistTourImpl()
    }

    @Provides
    @Singleton
    fun provideReviewRepo(localDatabase: LocalDatabase): ReviewRepo {
        return ReviewImpl(localDatabase)
    }

    @Provides
    @Singleton
    fun provideListRepo(localDatabase: LocalDatabase): ListRepo {
        return ListImpl(localDatabase)
    }

    @Provides
    @Singleton
    fun provideUserRepo(localDatabase: LocalDatabase): UserRepo {
        return UserImpl(localDatabase)
    }

    @Provides
    @Singleton
    fun provideEventDao(localDatabase: LocalDatabase): EventDao {
        return localDatabase.eventDao()
    }

    @Provides
    @Singleton
    fun provideEventsRepo(eventDao: EventDao): EventRepo {
        return EventImpl(eventDao)
    }

    @Provides
    @Singleton
    fun provideLocalDatabase(app: Application): LocalDatabase {
        return Room.databaseBuilder(app, LocalDatabase::class.java, "MusicBoxdDB").build()
    }
}