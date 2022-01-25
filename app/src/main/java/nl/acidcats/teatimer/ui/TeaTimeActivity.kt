package nl.acidcats.teatimer.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.ajalt.timberkt.Timber
import nl.acidcats.teatimer.R
import nl.acidcats.teatimer.alarm.AlarmService
import nl.acidcats.teatimer.databinding.ActivityMainBinding
import nl.acidcats.teatimer.util.AppShortcutUtil
import nl.acidcats.teatimer.util.StorageHelper
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.time.ExperimentalTime

@ExperimentalTime
class TeaTimeActivity : AppCompatActivity() {

    private val _updateRunnable = ::updateView
    private val _updateHandler = Handler(Looper.getMainLooper())
    private val storageHelper: StorageHelper by inject()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.stopButton.setOnClickListener {
            AlarmService.cancelAlarmService(this)

            _updateHandler.removeCallbacks(_updateRunnable)

            finish()
        }

        binding.settingsButton.setOnClickListener {
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
        if (!storageHelper.alarmState.isAlarmRunning) {
            showStopped()

            return
        }

        var secondsLeft = (storageHelper.alarmState.alarmEndTime - Calendar.getInstance().timeInMillis) / 1000L
        val minutesLeft = secondsLeft / 60L
        secondsLeft %= 60L
        binding.timeText.text = getString(R.string.time_left, minutesLeft, secondsLeft)

        _updateHandler.removeCallbacks(_updateRunnable)
        _updateHandler.postDelayed(_updateRunnable, 500L)
    }

    private fun showStopped() {
        binding.timeText.text = getString(R.string.alarm_stopped)
        binding.stopButton.visibility = View.GONE
    }
}
