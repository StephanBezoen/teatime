package nl.acidcats.teatimer.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.github.ajalt.timberkt.Timber
import nl.acidcats.teatimer.ui.TeaTimeActivity
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Receiver for alarms
 */

class AlarmReceiver : BroadcastReceiver(), KoinComponent {

    private val alarmHelper: AlarmHelper by inject()

    override fun onReceive(context: Context, intent: Intent) {
        Timber.d { "onReceive: " }

        alarmHelper.handleAlarm(TeaTimeActivity::class.java)
    }
}
