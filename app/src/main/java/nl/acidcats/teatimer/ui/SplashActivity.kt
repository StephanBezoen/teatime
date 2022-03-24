package nl.acidcats.teatimer.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.ajalt.timberkt.Timber
import nl.acidcats.teatimer.R
import nl.acidcats.teatimer.TimerActionReceiver
import nl.acidcats.teatimer.helpers.AppShortcutHelper
import nl.acidcats.teatimer.helpers.ConfigHelper
import nl.acidcats.teatimer.helpers.StorageHelper
import nl.acidcats.teatimer.util.IntentKey
import nl.acidcats.teatimer.util.getIntExtra
import org.koin.android.ext.android.inject

/**
 * Created on 23/11/2017.
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val storageHelper: StorageHelper by inject()
    private val appShortcutHelper: AppShortcutHelper by inject()
    private val configHelper: ConfigHelper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appShortcutHelper.setupShortcuts()

        if (storageHelper.alarmState.isAlarmRunning) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            val duration = intent.getIntExtra(IntentKey.Duration, configHelper.defaultDuration)
            Timber.d { "duration = $duration" }

            applicationContext.sendBroadcast(
                TimerActionReceiver.getStartIntent(
                    context = applicationContext,
                    duration = duration
                )
            )
        }

        finish()
    }
}
