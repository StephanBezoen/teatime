package com.mediamonks.teatimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

/**
 * Created by stephan on 23/03/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");

        Toast.makeText(context, "Tea is done!", Toast.LENGTH_LONG).show();

        NotificationUtil.cancelNotification(context, NotificationId.ID_TIMER_STARTED);

        NotificationUtil.showNotification(
                context,
                NotificationId.ID_TIMER_DONE,
                context.getString(R.string.tea_done_title),
                context.getString(R.string.tea_done_message), true);

        Prefs.putBoolean(PrefKeys.ALARM_RUNNING, false);

        new Handler().postDelayed(() -> NotificationUtil.cancelNotification(context, NotificationId.ID_TIMER_DONE), 5000);
    }

}
