package nl.acidcats.teatimer.alarm

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.StringRes
import kotlinx.coroutines.*
import nl.acidcats.teatimer.R
import nl.acidcats.teatimer.ui.TeaTimeActivity
import nl.acidcats.teatimer.util.NotificationUtil
import nl.acidcats.teatimer.util.ScreenUtil
import nl.acidcats.teatimer.util.SoundUtil
import nl.acidcats.teatimer.util.StorageHelper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
class AlarmService : Service(), CoroutineScope, KoinComponent {
    private val coroutineJob: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob

    private val storageHelper: StorageHelper by inject()
    private var timerJob: Job? = null

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val KEY_DURATION = "duration"
        private const val ACTION_STOP = "stop"

        fun startAlarmService(context: Context, durationInMinutes: Int) {
            Intent(context, AlarmService::class.java).also { intent ->
                intent.putExtra(KEY_DURATION, durationInMinutes)

                context.startForegroundService(intent)
            }
        }

        fun stopAlarmService(context: Context) {
            Intent(context, AlarmService::class.java).also { intent ->
                intent.action = ACTION_STOP

                context.startService(intent)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        timerJob?.cancel()

        if (intent?.action?.equals(ACTION_STOP) == true) {
            cancelTimer()

            return START_NOT_STICKY
        }

        val minutes = intent?.getIntExtra(KEY_DURATION, 5) ?: return START_REDELIVER_INTENT

        onTimerStarted(minutes)

        timerJob = launch {
            delay(minutes * 60_000L)

            withContext(Dispatchers.Main) {
                onTimerDone()
            }
        }

        return START_STICKY
    }

    private fun onTimerStarted(minutes: Int) {
        val calendar = Calendar.getInstance().apply { add(Calendar.MINUTE, minutes) }

        updateAlarmPreferences(calendar.timeInMillis, true)

        showToast(R.string.timer_started)

        val notification = NotificationUtil.createNotification(
            context = this,
            channelIdId = R.string.notification_channel_id,
            title = getString(R.string.timer_started_title),
            message = getString(R.string.timer_started_message, SimpleDateFormat.getTimeInstance().format(calendar.time)),
            cls = TeaTimeActivity::class.java,
            isImportant = false
        )
        startForeground(1, notification)
    }

    private fun onTimerDone() {
        updateAlarmPreferences(0, false)

        ScreenUtil.wakeScreen(this, Duration.seconds(2))

        NotificationUtil.showNotification(
            context = this,
            id = 2,
            title = getString(R.string.tea_done_title),
            message = getString(R.string.tea_done_message),
            isImportant = true,
            channelIdId = R.string.alarm_channel_id,
            cls = TeaTimeActivity::class.java
        )

        SoundUtil.playSound(this)

        showToast(R.string.tea_done_title, Toast.LENGTH_LONG)

        stopSelf()
    }

    private fun cancelTimer() {
        timerJob?.cancel()

        updateAlarmPreferences(0, false)

        showToast(R.string.timer_stopped)

        stopSelf()
    }

    private fun showToast(@StringRes messageId: Int, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, messageId, duration).show()
    }

    private fun updateAlarmPreferences(endTime: Long, timerStarted: Boolean) {
        storageHelper.alarmEndTime = endTime
        storageHelper.isAlarmRunning = timerStarted
    }

    override fun onDestroy() {
        coroutineJob.cancel()
    }
}