package nl.acidcats.teatimer.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import nl.acidcats.teatimer.alarm.AlarmService
import nl.acidcats.teatimer.util.AppShortcutUtil
import nl.acidcats.teatimer.util.BundleUtil
import nl.acidcats.teatimer.util.StorageHelper
import org.koin.android.ext.android.inject
import kotlin.time.ExperimentalTime

/**
 * Created on 23/11/2017.
 */
@SuppressLint("CustomSplashScreen")
@ExperimentalTime
class SplashActivity : AppCompatActivity() {

    private val storageHelper: StorageHelper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppShortcutUtil.setupShortcuts(this)

        if (storageHelper.isAlarmRunning) {
            startActivity(Intent(this, TeaTimeActivity::class.java))
        } else {
            val minutes = intent?.extras?.let { extras ->
                BundleUtil.getBundleValue(extras, AppShortcutUtil.KEY_TIME, DEFAULT_TEA_TIME_MINS)
            } ?: DEFAULT_TEA_TIME_MINS

            AlarmService.startAlarmService(this, durationInMinutes = minutes)
        }

        finish()
    }

    companion object {
        private const val DEFAULT_TEA_TIME_MINS = 5
    }
}
