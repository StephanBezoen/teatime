package nl.acidcats.teatimer.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.github.ajalt.timberkt.Timber

import nl.acidcats.teatimer.ui.TeaTimeActivity

/**
 * Receiver for alarms
 */

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Timber.d { "onReceive: " }

        AlarmHelper.handleAlarm(context, TeaTimeActivity::class.java)
    }
}
