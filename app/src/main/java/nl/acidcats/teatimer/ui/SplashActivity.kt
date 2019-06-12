package nl.acidcats.teatimer.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import nl.acidcats.teatimer.alarm.AlarmHelper
import nl.acidcats.teatimer.util.AppShortcutUtil
import nl.acidcats.teatimer.util.BundleUtil

/**
 * Created on 23/11/2017.
 */

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppShortcutUtil.setupShortcuts(this)

        if (AlarmHelper.isAlarmRunning) {
            startActivity(Intent(this, TeaTimeActivity::class.java))
        } else {
            startAlarm()
        }

        finish()
    }

    private fun startAlarm() {
        val timerMins = intent?.extras?.let {
            BundleUtil.getBundleValue(it, AppShortcutUtil.KEY_TIME, DEFAULT_TEA_TIME_MINS)
        } ?: DEFAULT_TEA_TIME_MINS

        AlarmHelper.startAlarm(this, timerMins, TeaTimeActivity::class.java)
    }

    companion object {
        private const val DEFAULT_TEA_TIME_MINS = 5
    }
}
