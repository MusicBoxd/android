package musicboxd.android.data

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import musicboxd.android.ui.notifications.NewReleaseNotificationService
import musicboxd.android.ui.notifications.model.NewReleaseNotificationModel


class RefreshReleasesWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    private val newReleaseNotificationService = NewReleaseNotificationService(appContext)

    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {
        newReleaseNotificationService.showNotification(
            newReleaseNotificationModel = NewReleaseNotificationModel(
                title = "Sample Notification",
                description = "Sample Description"
            )
        )
        return Result.success()
    }
}
