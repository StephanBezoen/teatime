package nl.acidcats.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.acidcats.alarm.AlarmHelper;
import nl.acidcats.teatimer.R;
import timber.log.Timber;

public class TeaTimeActivity extends AppCompatActivity {

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

        if (!AlarmHelper.isAlarmRunning()) {
            AlarmHelper.startAlarm(this, TeaTimeActivity.class);

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

    @OnClick(R.id.btn_stop)
    void onStopButtonClick() {
        AlarmHelper.stopAlarm(this);

        _updateHandler.removeCallbacks(_updateRunnable);

        finish();
    }
}
