@file:Suppress("SpellCheckingInspection")

package nl.acidcats.teatimer.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent

interface AlarmHelper {
    fun startAlarm(endTimeMs: Long)

    fun stopAlarm()
}

class AlarmHelperImpl(
    private val context: Context,
) : AlarmHelper {
    override fun startAlarm(endTimeMs: Long) {
        val alarmManager = context.getSystemService(Service.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, endTimeMs, getPendingAlarmIntent())
    }

    override fun stopAlarm() {
        (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).cancel(getPendingAlarmIntent())
    }

    private fun getPendingAlarmIntent(): PendingIntent {
        val receiverIntent = Intent(context, AlarmReceiver::class.java)
        return PendingIntent.getBroadcast(context, 1, receiverIntent, PendingIntent.FLAG_IMMUTABLE)
    }
}
