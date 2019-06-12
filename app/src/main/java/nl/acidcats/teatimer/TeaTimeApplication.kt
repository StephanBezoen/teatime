package nl.acidcats.teatimer

import android.app.Application
import android.app.NotificationManager
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.Timber.DebugTree
import com.pixplicity.easyprefs.library.Prefs
import nl.acidcats.teatimer.util.NotificationUtil

/**
 * Application class for the TeaTimer application
 */

@Suppress("unused")
class TeaTimeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Prefs.initPrefs(this)

        Timber.plant(DebugTree())

        NotificationUtil.createNotificationChannel(this,
            R.string.notification_channel_id, R.string.notification_channel_name, R.string.notification_channel_description,
            isImportant = false, enableVibration = false, enableLights = false)

        NotificationUtil.createNotificationChannel(this,
            R.string.alarm_channel_id, R.string.alarm_channel_name, R.string.alarm_channel_description,
            isImportant = true, enableVibration = true, enableLights = true)
    }
}
