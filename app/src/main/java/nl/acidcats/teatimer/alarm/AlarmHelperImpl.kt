@file:Suppress("SpellCheckingInspection")

package nl.acidcats.teatimer.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.annotation.StringRes
import nl.acidcats.teatimer.R
import nl.acidcats.teatimer.util.NotificationUtil
import nl.acidcats.teatimer.util.SoundUtil
import nl.acidcats.teatimer.util.StorageHelper
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created on 31/01/2018.
 */

interface AlarmHelper {

    val isAlarmRunning: Boolean

    val timeLeft: Long

    fun startAlarm(timerMins: Int, actionClass: Class<*>)

    fun handleAlarm(actionClass: Class<*>)

    fun stopAlarm()
}

class AlarmHelperImpl(
        private val context: Context,
        private val storageHelper: StorageHelper,
) : AlarmHelper {

    override val isAlarmRunning: Boolean
        get() = storageHelper.isAlarmRunning

    override val timeLeft: Long
        get() = storageHelper.alarmEndTime - Calendar.getInstance().timeInMillis

    override fun startAlarm(timerMins: Int, actionClass: Class<*>) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, timerMins)
        val endTime = calendar.timeInMillis
        setAlarm(context, endTime)

        updateAlarmPreferences(endTime, true)

        val dateFormat = SimpleDateFormat.getTimeInstance()
        val timeDone = dateFormat.format(Date(endTime))
        NotificationUtil.showNotification(
                context = context,
                id = NOTIFICATION_ID_TIMER_STARTED,
                title = context.getString(R.string.timer_started_title),
                message = context.getString(R.string.timer_started_message, timeDone),
                channelIdId = R.string.notification_channel_id,
                cls = actionClass)

        showToast(context, R.string.timer_started)
    }

    override fun handleAlarm(actionClass: Class<*>) {
        clearAlarmPreferences()

        NotificationUtil.cancelNotification(context, NOTIFICATION_ID_TIMER_STARTED)

        NotificationUtil.showNotification(
                context,
                NOTIFICATION_ID_TIMER_DONE,
                context.getString(R.string.tea_done_title),
                context.getString(R.string.tea_done_message), true, R.string.alarm_channel_id, actionClass)

        showToast(context, R.string.tea_done_title, Toast.LENGTH_LONG)

        SoundUtil.playSound(context)
    }

    override fun stopAlarm() {
        if (!clearAlarm(context)) return

        clearAlarmPreferences()

        NotificationUtil.cancelNotification(context, NOTIFICATION_ID_TIMER_STARTED)

        showToast(context, R.string.timer_stopped)
    }

    private fun clearAlarmPreferences() {
        updateAlarmPreferences(0, false)
    }

    private fun updateAlarmPreferences(endTime: Long, timerStarted: Boolean) {
        storageHelper.alarmEndTime = endTime
        storageHelper.isAlarmRunning = timerStarted
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

    companion object {
        private const val NOTIFICATION_ID_TIMER_STARTED = 1
        private const val NOTIFICATION_ID_TIMER_DONE = 2
    }
}
