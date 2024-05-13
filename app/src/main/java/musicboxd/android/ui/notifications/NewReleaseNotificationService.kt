package musicboxd.android.ui.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import musicboxd.android.MainActivity
import musicboxd.android.R
import musicboxd.android.ui.notifications.model.NewReleaseNotificationModel

class NewReleaseNotificationService(private val context: Context) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(newReleaseNotificationModel: NewReleaseNotificationModel) {
        val intent = Intent(context, MainActivity::class.java)
        val albumScreenPendingIntent = PendingIntent.getActivity(
            context, 1, intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val addToListenLaterPendingIntent = PendingIntent.getBroadcast(
            context,
            2,
            Intent(
                context,
                NewReleasesNotificationBroadCastReceiver::class.java,
            ),
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, "1")
            .setSmallIcon(R.drawable.lastfm)
            .setContentTitle(newReleaseNotificationModel.title)
            .setContentText(newReleaseNotificationModel.description)
            .setContentIntent(albumScreenPendingIntent)
            .addAction(
                R.drawable.spotify_logo,
                "Add to Listen Later",
                addToListenLaterPendingIntent
            )
            .build()

        notificationManager.notify(1, notification)
    }
}