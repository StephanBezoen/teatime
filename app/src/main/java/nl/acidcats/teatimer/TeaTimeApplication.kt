package nl.acidcats.teatimer

import android.app.Application
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.Timber.DebugTree
import nl.acidcats.teatimer.di.helperModule
import nl.acidcats.teatimer.helpers.NotificationHelper
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Application class for the TeaTimer application
 */

class TeaTimeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(DebugTree())

        startKoin {
            androidLogger()
            androidContext(this@TeaTimeApplication)
            koin.loadModules(listOf(helperModule))
        }

        initNotificationChannels()
    }

    private fun initNotificationChannels() {
        val helper = getKoin().get<NotificationHelper>()
        helper.createNotificationChannel(
            channelIdId = R.string.notification_channel_id,
            nameId = R.string.notification_channel_name,
            descId = R.string.notification_channel_description,
            isImportant = false,
            enableVibration = false,
            enableLights = false
        )

        helper.createNotificationChannel(
            channelIdId = R.string.alarm_channel_id,
            nameId = R.string.alarm_channel_name,
            descId = R.string.alarm_channel_description,
            isImportant = true,
            enableVibration = true,
            enableLights = true
        )
    }
}
