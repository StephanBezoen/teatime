@file:Suppress("SpellCheckingInspection")

package nl.acidcats.teatimer.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.widget.Toast
import androidx.annotation.StringRes
import com.pixplicity.easyprefs.library.Prefs
import nl.acidcats.teatimer.R
import nl.acidcats.teatimer.util.NotificationUtil
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created on 31/01/2018.
 */

object AlarmHelper {

    private const val NOTIFICATION_ID_TIMER_STARTED = 1
    private const val NOTIFICATION_ID_TIMER_DONE = 2

    private const val PREFKEY_ALARM_RUNNING = "prefkey_alarmRunning"
    private const val PREFKEY_ALARM_END_TIME = "prefkey_alarmEndTime"

    val isAlarmRunning: Boolean
        get() = Prefs.contains(PREFKEY_ALARM_RUNNING) && Prefs.getBoolean(PREFKEY_ALARM_RUNNING, false)

    val timeLeft: Long
        get() = Prefs.getLong(PREFKEY_ALARM_END_TIME, 0L) - Calendar.getInstance().timeInMillis

    fun startAlarm(context: Context, timerMins: Int, actionClass: Class<*>) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, timerMins)
        val endTime = calendar.timeInMillis
        setAlarm(context, endTime)

        updateAlarmPreferences(endTime, true)

        val dateFormat = SimpleDateFormat.getTimeInstance()
        val timeDone = dateFormat.format(Date(endTime))
        NotificationUtil.showNotification(context, NOTIFICATION_ID_TIMER_STARTED,
            context.getString(R.string.timer_started_title),
            context.getString(R.string.timer_started_message, timeDone),
            R.string.notification_channel_id,
            actionClass)

        showToast(context, R.string.timer_started)
    }

    fun handleAlarm(context: Context, actionClass: Class<*>) {
        clearAlarmPreferences()

        NotificationUtil.cancelNotification(context, NOTIFICATION_ID_TIMER_STARTED)

        NotificationUtil.showNotification(
            context,
            NOTIFICATION_ID_TIMER_DONE,
            context.getString(R.string.tea_done_title),
            context.getString(R.string.tea_done_message), true, R.string.alarm_channel_id, actionClass)

        Handler().postDelayed({ NotificationUtil.cancelNotification(context, NOTIFICATION_ID_TIMER_DONE) }, 5000)

        showToast(context, R.string.tea_done_title, Toast.LENGTH_LONG)
    }

    fun stopAlarm(context: Context) {
        if (!clearAlarm(context)) return

        clearAlarmPreferences()

        NotificationUtil.cancelNotification(context, NOTIFICATION_ID_TIMER_STARTED)

        showToast(context, R.string.timer_stopped)
    }

    private fun clearAlarmPreferences() {
        updateAlarmPreferences(0, false)
    }

    private fun updateAlarmPreferences(endTime: Long, timerStarted: Boolean) {
        Prefs.putLong(PREFKEY_ALARM_END_TIME, endTime)
        Prefs.putBoolean(PREFKEY_ALARM_RUNNING, timerStarted)
    }

    private fun setAlarm(context: Context, endTime: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, endTime, getAlarmReceiverIntent(context))
    }

    private fun clearAlarm(context: Context): Boolean {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(getAlarmReceiverIntent(context))

        return true
    }

    private fun getAlarmReceiverIntent(context: Context): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java)
        return PendingIntent.getBroadcast(context, 1, intent, 0)
    }

    private fun showToast(context: Context, @StringRes messageId: Int, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, messageId, duration).show()
    }
}
