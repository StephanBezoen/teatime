package nl.acidcats.teatimer.alarm

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.StringRes
import com.github.ajalt.timberkt.Timber
import nl.acidcats.teatimer.R
import nl.acidcats.teatimer.ui.TeaTimeActivity
import nl.acidcats.teatimer.util.*
import nl.acidcats.teatimer.widget.updateAppWidget
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@ExperimentalTime
class AlarmService : Service(), KoinComponent {

    private val storageHelper: StorageHelper by inject()
    private val alarmHelper: AlarmHelper by inject()

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private val DurationKey = Key("duration")
        private val WidgetIdKey = Key("widgetId")

        private val StartAction = Action("start")
        private val CancelAction = Action("cancel")
        private val DoneAction = Action("done")

        fun getAlarmServiceIntent(context: Context, durationInMinutes: Int, appWidgetId: Int? = null) =
            Intent(context, AlarmService::class.java).apply {
                action = StartAction.value
                putExtra(DurationKey.value, durationInMinutes)
                appWidgetId?.let { id -> putExtra(WidgetIdKey.value, id) }
            }

        fun startAlarmService(context: Context, durationInMinutes: Int) {
            context.startService(getAlarmServiceIntent(context, durationInMinutes))
        }

        fun cancelAlarmService(context: Context) {
            startService(context, CancelAction)
        }

        fun onAlarmDone(context: Context) {
            startService(context, DoneAction)
        }

        private fun startService(context: Context, action: Action) {
            context.startService(Intent(context, AlarmService::class.java).apply { this.action = action.value })
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d { "Intent action = ${intent?.action}" }

        return when (intent?.action) {
            StartAction.value -> {
                startTimer(
                    minutes = intent.getIntExtra(DurationKey.value, 5),
                    widgetId = intent.getIntExtra(WidgetIdKey.value, 0)
                )

                START_STICKY
            }
            CancelAction.value -> {
                cancelTimer()

                START_NOT_STICKY
            }
            DoneAction.value -> {
                onTimerDone()

                START_NOT_STICKY
            }
            else -> START_NOT_STICKY
        }
    }

    private fun startTimer(minutes: Int, widgetId: Int) {
        Timber.d { "Starting timer for $minutes minutes" }

        val endTimeMs = Calendar.getInstance().apply { add(Calendar.MINUTE, minutes) }.timeInMillis

        storageHelper.alarmState = AlarmState(true, endTimeMs)

        alarmHelper.startAlarm(endTimeMs)

        showToast(R.string.timer_started)

        updateWidget(widgetId)

        NotificationUtil.showNotification(
            context = this,
            id = 1,
            channelIdId = R.string.notification_channel_id,
            title = getString(R.string.timer_started_title),
            message = getString(R.string.timer_started_message, SimpleDateFormat.getTimeInstance().format(Date(endTimeMs))),
            cls = TeaTimeActivity::class.java,
            isImportant = false
        )
    }

    private fun updateWidget(widgetId: Int) {
        if (widgetId == 0) return

        updateAppWidget(this, AppWidgetManager.getInstance(this), widgetId)
    }

    private fun onTimerDone() {
        storageHelper.alarmState = StoppedAlarm

        ScreenUtil.wakeScreen(this, 2.seconds)

        NotificationUtil.cancelNotification(this, 1)

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
        alarmHelper.stopAlarm()

        NotificationUtil.cancelNotification(this, 1)

        storageHelper.alarmState = StoppedAlarm

        showToast(R.string.timer_stopped)

        stopSelf()
    }

    private fun showToast(@StringRes messageId: Int, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, messageId, duration).show()
    }

    @JvmInline
    value class Action(val value: String)

    @JvmInline
    value class Key(val value: String)
}