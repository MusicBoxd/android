package musicboxd.android.data.remote.api.musicboxd

import musicboxd.android.data.remote.api.musicboxd.model.MusicBoxdLoginDTO
import musicboxd.android.data.remote.api.musicboxd.model.MusicBoxdTokenDTO
import musicboxd.android.data.remote.api.musicboxd.model.ReviewDTO
import musicboxd.android.data.remote.api.musicboxd.model.review.MusicBoxdPublicReviews
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MusicBoxdAPIService {
    @POST("api/auth/review/add")
    suspend fun postANewReview(
        @Body reviewDTO: ReviewDTO,
        @Header("Authorization") authorization: String
    ): Response<Unit>

    @POST("api/auth/login")
    suspend fun getUserToken(@Body musicBoxdLoginDTO: MusicBoxdLoginDTO): MusicBoxdTokenDTO

    @GET("api/home/reviews")
    suspend fun getReviews(): List<MusicBoxdPublicReviews>
}