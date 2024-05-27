package musicboxd.android.ui.notifications.service

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.work.impl.utils.ForceStopRunnable.BroadcastReceiver

@SuppressLint("RestrictedApi")
class NewReleasesNotificationBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("10MinMail", "Triggered")
        Toast.makeText(context.applicationContext, "Added to listen later", Toast.LENGTH_SHORT)
            .show()
    }
}