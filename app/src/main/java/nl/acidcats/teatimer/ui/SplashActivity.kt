package nl.acidcats.teatimer.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import nl.acidcats.teatimer.alarm.AlarmHelper
import nl.acidcats.teatimer.util.AppShortcutUtil
import nl.acidcats.teatimer.util.BundleUtil
import org.koin.android.ext.android.inject


/**
 * Created on 23/11/2017.
 */

class SplashActivity : AppCompatActivity() {

    private val alarmHelper: AlarmHelper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppShortcutUtil.setupShortcuts(this)

        if (alarmHelper.isAlarmRunning) {
            startActivity(Intent(this, TeaTimeActivity::class.java))
        } else {
            startAlarm()

            val goToHomeIntent = Intent(Intent.ACTION_MAIN)
            goToHomeIntent.addCategory(Intent.CATEGORY_HOME)
            goToHomeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(goToHomeIntent)

        }

        finish()
    }

    private fun startAlarm() {
        val timerMins = intent?.extras?.let { extras ->
            BundleUtil.getBundleValue(extras, AppShortcutUtil.KEY_TIME, DEFAULT_TEA_TIME_MINS)
        } ?: DEFAULT_TEA_TIME_MINS

        alarmHelper.startAlarm(timerMins, TeaTimeActivity::class.java)
    }

    companion object {
        private const val DEFAULT_TEA_TIME_MINS = 5
    }
}
