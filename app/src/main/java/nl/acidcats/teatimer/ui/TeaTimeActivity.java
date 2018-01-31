package nl.acidcats.teatimer.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.acidcats.teatimer.alarm.AlarmHelper;
import nl.acidcats.teatimer.R;
import timber.log.Timber;

public class TeaTimeActivity extends AppCompatActivity {

    @BindView(R.id.tv_time)
    TextView _timeText;
    @BindView(R.id.btn_stop)
    Button _stopButton;
    @BindView(R.id.settings_button)
    ImageView _settingsButton;

    private final Runnable _updateRunnable = this::updateView;
    private final Handler _updateHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (!AlarmHelper.isAlarmRunning()) {
            AlarmHelper.startAlarm(this, TeaTimeActivity.class);

            finish();
        }

        _stopButton.setOnClickListener(v -> onStopButtonClick());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _settingsButton.setVisibility(View.VISIBLE);
            _settingsButton.setOnClickListener(v -> goNotificationSettings());
        } else {
            _settingsButton.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void goNotificationSettings() {
        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, getString(R.string.notification_channel_id));
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        startActivity(intent);
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
        Timber.d("updateView: ");

        if (!AlarmHelper.isAlarmRunning()) {
            showStopped();

            return;
        }

        long secondsLeft = AlarmHelper.getTimeLeft() / 1000L;
        long minutesLeft = secondsLeft / 60L;
        secondsLeft = secondsLeft % 60L;
        _timeText.setText(getString(R.string.time_left, minutesLeft, secondsLeft));

        _updateHandler.removeCallbacks(_updateRunnable);
        _updateHandler.postDelayed(_updateRunnable, 500L);
    }

    private void showStopped() {
        _timeText.setText(getString(R.string.alarm_stopped));
        _stopButton.setVisibility(View.GONE);
    }

    private void onStopButtonClick() {
        AlarmHelper.stopAlarm(this);

        _updateHandler.removeCallbacks(_updateRunnable);

        finish();
    }
}
