package nl.acidcats.teatimer.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlin.time.ExperimentalTime

/**
 * Receiver for alarms
 */

@ExperimentalTime
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        AlarmService.onAlarmDone(context)
    }
}
