package nl.acidcats.teatimer.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import nl.acidcats.teatimer.ui.TeaTimeActivity;
import timber.log.Timber;

/**
 * Receiver for alarms
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.d("onReceive: ");

        AlarmHelper.handleAlarm(context, TeaTimeActivity.class);
    }
}
