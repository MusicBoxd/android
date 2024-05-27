package musicboxd.android.data

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import musicboxd.android.ui.notifications.model.NewReleaseNotificationModel
import musicboxd.android.ui.notifications.service.NewReleaseNotificationService
import okhttp3.OkHttpClient
import okhttp3.Request

@Serializable
data class ISSDTO(
    val message: String,
    @SerialName("iss_position")
    val issPosition: IssPosition,
    val timestamp: Long,
)

@Serializable
data class IssPosition(
    val latitude: String,
    val longitude: String,
)


class RefreshReleasesWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    private val newReleaseNotificationService = NewReleaseNotificationService(appContext)

    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {
        try {
            OkHttpClient().newCall(
                Request.Builder().get()
                    .url("http://api.open-notify.org/iss-now.json")
                    .build()
            ).execute().body?.string()?.let { rawString ->
                val issDTO = Json {
                    ignoreUnknownKeys = true
                }.decodeFromString<ISSDTO>(rawString)
                newReleaseNotificationService.showNotification(
                    newReleaseNotificationModel = NewReleaseNotificationModel(
                        title = issDTO.message,
                        description = "${issDTO.issPosition.longitude}, ${issDTO.issPosition.latitude}"
                    )
                )
            }
            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure()
        }

    }
}
