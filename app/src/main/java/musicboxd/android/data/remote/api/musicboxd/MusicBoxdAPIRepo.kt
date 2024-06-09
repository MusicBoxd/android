package musicboxd.android.data.remote.api.musicboxd

import musicboxd.android.data.remote.api.APIResult
import musicboxd.android.data.remote.api.musicboxd.model.MusicBoxdLoginDTO
import musicboxd.android.data.remote.api.musicboxd.model.MusicBoxdTokenDTO
import musicboxd.android.data.remote.api.musicboxd.model.ReviewDTO
import musicboxd.android.data.remote.api.musicboxd.model.list.ListDTO
import musicboxd.android.data.remote.api.musicboxd.model.review.MusicBoxdPublicReviews
import musicboxd.android.data.remote.api.musicboxd.model.user.SignUpDTO
import musicboxd.android.data.remote.api.musicboxd.model.user.User
import retrofit2.http.Body

interface MusicBoxdAPIRepo {
    suspend fun postANewReview(@Body reviewDTO: ReviewDTO, authorization: String): APIResult<String>
    suspend fun postANewList(
        localListId: Long,
        listDTO: ListDTO,
        authorization: String
    ): APIResult<String>

    suspend fun getPublicLists(authorization: String): APIResult<List<ListDTO>>

    suspend fun getReviews(): APIResult<List<MusicBoxdPublicReviews>>
    suspend fun createANewUser(@Body signUpDTO: SignUpDTO): APIResult<User>

    suspend fun getUserToken(@Body musicBoxdLoginDTO: MusicBoxdLoginDTO): APIResult<MusicBoxdTokenDTO>
}