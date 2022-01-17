package nl.acidcats.teatimer.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import nl.acidcats.teatimer.ui.TeaTimeActivity
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Receiver for alarms
 */

class AlarmReceiver : BroadcastReceiver(), KoinComponent {

    private val alarmHelper: AlarmHelper by inject()

    override fun onReceive(context: Context, intent: Intent) {
        alarmHelper.handleAlarm(TeaTimeActivity::class.java)
    }
}
