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

class TeaTimeActivity : AppCompatActivity() {

    private val _updateRunnable = Runnable { this.updateView() }
    private val _updateHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        stopButton.setOnClickListener { onStopButtonClick() }
        settingsButton.setOnClickListener { goNotificationSettings() }
    }

    override fun onNewIntent(newIntent: Intent) {
        super.onNewIntent(newIntent)

        Timber.d { "onNewIntent: $intent" }

        if (intent != null) {
            Timber.d { "onNewIntent: ${intent.getIntExtra(AppShortcutUtil.KEY_TIME, -1)}" }
        }
    }

    private fun goNotificationSettings() {
        val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, getString(R.string.notification_channel_id))
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        startActivity(intent)
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

        if (!AlarmHelper.isAlarmRunning) {
            showStopped()

            return
        }

        var secondsLeft = AlarmHelper.timeLeft / 1000L
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

    private fun onStopButtonClick() {
        AlarmHelper.stopAlarm(this)

        _updateHandler.removeCallbacks(_updateRunnable)

        finish()
    }
}