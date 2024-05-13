package musicboxd.android.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings

fun showNotificationSettings(context: Context, channelId: String? = null) {
    val notificationSettingsIntent = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> Intent().apply {
            action = when (channelId) {
                null -> Settings.ACTION_APP_NOTIFICATION_SETTINGS
                else -> Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS
            }
            channelId?.let { putExtra(Settings.EXTRA_CHANNEL_ID, it) }
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P /*28*/) {
                flags += Intent.FLAG_ACTIVITY_NEW_TASK
            }
        }

        else -> null
    }
    notificationSettingsIntent?.let(context::startActivity)
}