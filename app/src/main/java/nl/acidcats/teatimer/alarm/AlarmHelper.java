package nl.acidcats.teatimer.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import nl.acidcats.teatimer.R;
import nl.acidcats.teatimer.util.NotificationUtil;

/**
 * Created on 31/01/2018.
 */

public class AlarmHelper {

    private static final int NOTIFICATION_ID_TIMER_STARTED = 1;
    private static final int NOTIFICATION_ID_TIMER_DONE = 2;

    private static final String PREFKEY_ALARM_RUNNING = "prefkey_alarmRunning";
    private static final String PREFKEY_ALARM_END_TIME = "prefkey_alarmEndTime";

    private static final int DEFAULT_TEA_TIME_SECONDS = 5 * 60;

    public static void startAlarm(Context context, Class<?> actionClass) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, DEFAULT_TEA_TIME_SECONDS);
        long endTime = calendar.getTimeInMillis();
        if (!setAlarm(context, endTime)) return;

        updateAlarmPreferences(endTime, true);

        DateFormat dateFormat = SimpleDateFormat.getTimeInstance();
        String timeDone = dateFormat.format(new Date(endTime));
        NotificationUtil.showNotification(context, NOTIFICATION_ID_TIMER_STARTED,
                context.getString(R.string.timer_started_title),
                context.getString(R.string.timer_started_message, timeDone),
                R.string.notification_channel_id,
                actionClass);

        showToast(context, R.string.timer_started);
    }

    static void handleAlarm(Context context, Class<?> actionClass) {
        clearAlarmPreferences();

        NotificationUtil.cancelNotification(context, NOTIFICATION_ID_TIMER_STARTED);

        NotificationUtil.showNotification(
                context,
                NOTIFICATION_ID_TIMER_DONE,
                context.getString(R.string.tea_done_title),
                context.getString(R.string.tea_done_message), true, R.string.notification_channel_id, actionClass);

        new Handler().postDelayed(() -> NotificationUtil.cancelNotification(context, NOTIFICATION_ID_TIMER_DONE), 5000);

        showToast(context, R.string.tea_done_title, Toast.LENGTH_LONG);
    }

    public static void stopAlarm(Context context) {
        if (!clearAlarm(context)) return;

        clearAlarmPreferences();

        NotificationUtil.cancelNotification(context, NOTIFICATION_ID_TIMER_STARTED);

        showToast(context, R.string.timer_stopped);
    }

    private static void clearAlarmPreferences() {
        updateAlarmPreferences(0, false);
    }

    private static void updateAlarmPreferences(long endTime, boolean timerStarted) {
        Prefs.putLong(PREFKEY_ALARM_END_TIME, endTime);
        Prefs.putBoolean(PREFKEY_ALARM_RUNNING, timerStarted);
    }

    private static boolean setAlarm(Context context, long endTime) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, endTime, getAlarmReceiverIntent(context));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, endTime, getAlarmReceiverIntent(context));
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, endTime, getAlarmReceiverIntent(context));
        }

        return true;
    }

    private static boolean clearAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return false;

        alarmManager.cancel(getAlarmReceiverIntent(context));

        return true;
    }

    public static boolean isAlarmRunning() {
        return Prefs.contains(PREFKEY_ALARM_RUNNING) && Prefs.getBoolean(PREFKEY_ALARM_RUNNING, false);
    }

    public static long getTimeLeft() {
        return Prefs.getLong(PREFKEY_ALARM_END_TIME, 0L) - Calendar.getInstance().getTimeInMillis();
    }

    private static PendingIntent getAlarmReceiverIntent(Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        return PendingIntent.getBroadcast(context, 1, intent, 0);
    }

    private static void showToast(Context context, @StringRes int messageId, int duration) {
        Toast.makeText(context, messageId, duration).show();
    }

    private static void showToast(Context context, @StringRes int messageId) {
        showToast(context, messageId, Toast.LENGTH_SHORT);
    }
}
