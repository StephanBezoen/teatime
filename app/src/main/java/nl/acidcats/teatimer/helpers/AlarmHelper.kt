package nl.acidcats.teatimer.helpers

import android.app.AlarmManager
import android.app.Service
import android.content.Context
import nl.acidcats.teatimer.TimerActionReceiver

interface AlarmHelper {
    fun startAlarm(endTimeMs: Long)

    fun stopAlarm()
}

class AlarmHelperImpl(
    private val context: Context,
) : AlarmHelper {
    override fun startAlarm(endTimeMs: Long) {
        (context.getSystemService(Service.ALARM_SERVICE) as AlarmManager)
            .setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, endTimeMs, TimerActionReceiver.getDonePendingIntent(context))
    }

    override fun stopAlarm() {
        (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
            .cancel(TimerActionReceiver.getDonePendingIntent(context))
    }
}
