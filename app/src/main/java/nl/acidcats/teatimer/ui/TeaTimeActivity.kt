package nl.acidcats.teatimer.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.ajalt.timberkt.Timber
import kotlinx.android.synthetic.main.activity_main.*
import nl.acidcats.teatimer.R
import nl.acidcats.teatimer.alarm.AlarmHelper
import nl.acidcats.teatimer.util.AppShortcutUtil
import org.koin.android.ext.android.inject

class TeaTimeActivity : AppCompatActivity() {

    private val _updateRunnable = Runnable { this.updateView() }
    private val _updateHandler = Handler()
    private val alarmHelper: AlarmHelper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        stopButton.setOnClickListener {
            alarmHelper.stopAlarm()

            _updateHandler.removeCallbacks(_updateRunnable)

            finish()
        }

        settingsButton.setOnClickListener {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            startActivity(intent)
        }
    }

    override fun onNewIntent(newIntent: Intent) {
        super.onNewIntent(newIntent)

        Timber.d { "onNewIntent: $intent" }

        if (intent != null) {
            Timber.d { "onNewIntent: ${intent.getIntExtra(AppShortcutUtil.KEY_TIME, -1)}" }
        }
    }

    override fun onResume() {
        super.onResume()

        updateView()
    }

    override fun onPause() {
        super.onPause()

        _updateHandler.removeCallbacks(_updateRunnable)
    }

    private fun updateView() {
        Timber.d { "updateView: " }

        if (!alarmHelper.isAlarmRunning) {
            showStopped()

            return
        }

        var secondsLeft = alarmHelper.timeLeft / 1000L
        val minutesLeft = secondsLeft / 60L
        secondsLeft %= 60L
        timeText.text = getString(R.string.time_left, minutesLeft, secondsLeft)

        _updateHandler.removeCallbacks(_updateRunnable)
        _updateHandler.postDelayed(_updateRunnable, 500L)
    }

    private fun showStopped() {
        timeText.text = getString(R.string.alarm_stopped)
        stopButton.visibility = View.GONE
    }
}
