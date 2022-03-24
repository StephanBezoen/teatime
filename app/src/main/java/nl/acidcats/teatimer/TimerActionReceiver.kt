package nl.acidcats.teatimer

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import nl.acidcats.teatimer.helpers.*
import nl.acidcats.teatimer.ui.MainActivity
import nl.acidcats.teatimer.util.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.Duration.Companion.seconds

class TimerActionReceiver : BroadcastReceiver(), KoinComponent {

    private val storageHelper: StorageHelper by inject()
    private val alarmHelper: AlarmHelper by inject()
    private val notificationHelper: NotificationHelper by inject()
    private val screenHelper: ScreenHelper by inject()
    private val soundHelper: SoundHelper by inject()
    private val toastHelper: ToastHelper by inject()
    private val configHelper: ConfigHelper by inject()
    private val widgetHelper: WidgetHelper by inject()

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent.getAction() ?: return
        when (action) {
            IntentAction.StartAction -> {
                startTimer(
                    context = context,
                    duration = intent.getIntExtra(IntentKey.Duration, configHelper.defaultDuration),
                    widgetId = intent.getIntExtra(IntentKey.WidgetId, 0)
                )
            }
            IntentAction.CancelAction -> cancelTimer()
            IntentAction.DoneAction -> onTimerDone(context)
        }
    }

    private fun startTimer(context: Context, duration: Int, widgetId: Int) {
        val calendarField = when (configHelper.durationUnit) {
            DurationUnit.SECONDS -> Calendar.SECOND
            DurationUnit.MINUTES -> Calendar.MINUTE
        }
        val endTimeMs = Calendar.getInstance().apply { add(calendarField, duration) }.timeInMillis

        storageHelper.alarmState = AlarmState(true, endTimeMs)

        alarmHelper.startAlarm(endTimeMs)

        toastHelper.showToast(R.string.timer_started)

        widgetHelper.updateWidget(widgetId)

        notificationHelper.showNotification(
            id = NotificationHelper.NotificationId.RUNNING,
            channelIdId = R.string.notification_channel_id,
            title = context.getString(R.string.timer_started_title),
            message = context.getString(R.string.timer_started_message, context.getString(R.string.food_name), SimpleDateFormat.getTimeInstance().format(Date(endTimeMs))),
            cls = MainActivity::class.java,
            isImportant = false
        )
    }

    private fun cancelTimer() {
        alarmHelper.stopAlarm()

        toastHelper.showToast(R.string.timer_stopped)

        onAlarmStopped()
    }

    private fun onTimerDone(context: Context) {
        screenHelper.wakeScreen(2.seconds)

        notificationHelper.showNotification(
            id = NotificationHelper.NotificationId.DONE,
            title = context.getString(R.string.tea_done_title, context.getString(R.string.food_name)),
            message = context.getString(R.string.tea_done_message, context.getString(R.string.food_name)),
            isImportant = true,
            channelIdId = R.string.alarm_channel_id,
            cls = MainActivity::class.java
        )

        soundHelper.playSound()

        toastHelper.showToast(context.getString(R.string.tea_done_title, context.getString(R.string.food_name)), Toast.LENGTH_LONG)

        onAlarmStopped()
    }

    private fun onAlarmStopped() {
        notificationHelper.cancelNotification(NotificationHelper.NotificationId.RUNNING)

        storageHelper.alarmState = StoppedAlarm
    }

    companion object {
        fun getStartPendingIntent(
            context: Context,
            duration: Int? = null,
            widgetId: Int? = null,
        ): PendingIntent {
            val intent = getStartIntent(context, duration, widgetId)
            return PendingIntent.getBroadcast(context, IntentAction.StartAction.code, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }

        fun getStartIntent(
            context: Context,
            duration: Int? = null,
            widgetId: Int? = null,
        ) = Intent(context, TimerActionReceiver::class.java)
            .setAction(IntentAction.StartAction)
            .putExtra(IntentKey.WidgetId, widgetId)
            .putExtra(IntentKey.Duration, duration)

        fun getCancelIntent(context: Context) =
            Intent(context, TimerActionReceiver::class.java)
                .setAction(IntentAction.CancelAction)

        fun getDonePendingIntent(context: Context): PendingIntent {
            val intent = Intent(context, TimerActionReceiver::class.java).setAction(IntentAction.DoneAction)
            return PendingIntent.getBroadcast(context, IntentAction.DoneAction.code, intent, PendingIntent.FLAG_IMMUTABLE)
        }
    }
}
