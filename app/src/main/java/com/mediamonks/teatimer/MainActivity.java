package com.mediamonks.teatimer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.tv_time)
    TextView _timeText;
    @BindView(R.id.btn_stop)
    Button _stopButton;

    private Runnable _updateRunnable = this::updateView;
    private Handler _updateHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (!isAlarmRunning()) {
            startAlarm();

            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateView();
    }

    @Override
    protected void onPause() {
        super.onPause();

        _updateHandler.removeCallbacks(_updateRunnable);
    }

    private void updateView() {
        Log.d(TAG, "updateView: ");

        if (!isAlarmRunning()) {
            showStopped();

            return;
        }

        Calendar calendar = Calendar.getInstance();
        long timeLeft = Prefs.getLong(PrefKeys.ALARM_END_TIME, 0L) - calendar.getTimeInMillis();

        long secondsLeft = timeLeft / 1000L;
        long minutesLeft = secondsLeft / 60L;
        secondsLeft = secondsLeft % 60L;
        _timeText.setText(getString(R.string.time_left, minutesLeft, secondsLeft));

        _updateHandler.removeCallbacks(_updateRunnable);
        _updateHandler.postDelayed(_updateRunnable, AppConfig.VIEW_UPDATE_MS);
    }

    private void showStopped() {
        _timeText.setText(getString(R.string.alarm_stopped));
        _stopButton.setVisibility(View.GONE);
    }

    private void startAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, AppConfig.TEA_TIMER);
        long endTime = calendar.getTimeInMillis();

        Prefs.putLong(PrefKeys.ALARM_END_TIME, endTime);
        Prefs.putBoolean(PrefKeys.ALARM_RUNNING, true);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, endTime, getPendingIntent());

        Toast.makeText(this, "Timer started", Toast.LENGTH_SHORT).show();

        DateFormat dateFormat = SimpleDateFormat.getTimeInstance();
        String timeDone = dateFormat.format(new Date(endTime));
        NotificationUtil.showNotification(
                this,
                NotificationId.ID_TIMER_STARTED,
                getString(R.string.timer_started_title),
                getString(R.string.timer_started_message, timeDone));
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        return PendingIntent.getBroadcast(getBaseContext(), 1, intent, 0);
    }

    private boolean isAlarmRunning() {
        return Prefs.contains(PrefKeys.ALARM_RUNNING) && Prefs.getBoolean(PrefKeys.ALARM_RUNNING, false);
    }

    @OnClick(R.id.btn_stop)
    void onStopButtonClick() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent());

        Prefs.putBoolean(PrefKeys.ALARM_RUNNING, false);

        _updateHandler.removeCallbacks(_updateRunnable);

        NotificationUtil.cancelNotification(this, NotificationId.ID_TIMER_STARTED);

        Toast.makeText(this, "Timer stopped", Toast.LENGTH_SHORT).show();

        finish();
    }
}
