package musicboxd.android.data.remote.api.musicboxd

import musicboxd.android.data.remote.api.APIResult
import musicboxd.android.data.remote.api.musicboxd.model.MusicBoxdLoginDTO
import musicboxd.android.data.remote.api.musicboxd.model.MusicBoxdTokenDTO
import musicboxd.android.data.remote.api.musicboxd.model.ReviewDTO
import musicboxd.android.data.remote.api.musicboxd.model.list.ListDTO
import musicboxd.android.data.remote.api.musicboxd.model.review.MusicBoxdPublicReviews
import javax.inject.Inject

class MusicBoxdAPIImpl @Inject constructor(private val musicBoxdAPIService: MusicBoxdAPIService) :
    MusicBoxdAPIRepo {
    override suspend fun postANewReview(
        reviewDTO: ReviewDTO,
        authorization: String
    ): APIResult<String> {
        return try {
            musicBoxdAPIService.postANewReview(reviewDTO, "Bearer ".plus(authorization))
            APIResult.Success("Successful")
        } catch (_: Exception) {
            APIResult.Failure("Failed at postANewReview")
        }
    }

    override suspend fun postANewList(listDTO: ListDTO, authorization: String): APIResult<String> {
        return try {
            musicBoxdAPIService.postANewList(listDTO, "Bearer ".plus(authorization))
            APIResult.Success("Success")
        } catch (e: Exception) {
            e.printStackTrace()
            APIResult.Failure("Failed at postANewList")
        }
    }

    override suspend fun getPublicLists(authorization: String): APIResult<List<ListDTO>> {
        return try {
            APIResult.Success(musicBoxdAPIService.getPublicLists("Bearer ".plus(authorization)))
        } catch (e: Exception) {
            APIResult.Failure("Failed at getPublicLists")
        }
    }

    override suspend fun getReviews(): APIResult<List<MusicBoxdPublicReviews>> {
        return try {
            APIResult.Success(musicBoxdAPIService.getReviews())
        } catch (e: Exception) {
            APIResult.Failure("Failed at getReviews")
        }
    }

    override suspend fun getUserToken(musicBoxdLoginDTO: MusicBoxdLoginDTO): APIResult<MusicBoxdTokenDTO> {
        return try {
            val tokenData = musicBoxdAPIService.getUserToken(musicBoxdLoginDTO)
            APIResult.Success(tokenData)
        } catch (_: Exception) {
            APIResult.Failure("Failed at getUserToken")
        }
    }
}