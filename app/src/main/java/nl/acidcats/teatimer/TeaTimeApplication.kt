package nl.acidcats.teatimer

import android.app.Application
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.Timber.DebugTree
import nl.acidcats.teatimer.di.helperModule
import nl.acidcats.teatimer.util.NotificationUtil
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module

/**
 * Application class for the TeaTimer application
 */

@Suppress("unused")
class TeaTimeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(DebugTree())

        NotificationUtil.createNotificationChannel(this,
                R.string.notification_channel_id, R.string.notification_channel_name, R.string.notification_channel_description,
                isImportant = false, enableVibration = false, enableLights = false)

        NotificationUtil.createNotificationChannel(this,
                R.string.alarm_channel_id, R.string.alarm_channel_name, R.string.alarm_channel_description,
                isImportant = true, enableVibration = true, enableLights = true)

        startKoin {
            androidLogger()
            androidContext(this@TeaTimeApplication)
            modules(
                    listOf<Module>()
                            + helperModule
            )
        }
    }
}
