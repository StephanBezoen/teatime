package nl.acidcats.teatimer.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import nl.acidcats.teatimer.alarm.AlarmHelper;
import nl.acidcats.teatimer.util.AppShortcutUtil;
import nl.acidcats.teatimer.util.BundleUtil;

/**
 * Created on 23/11/2017.
 */

public class SplashActivity extends AppCompatActivity {

    private static final int DEFAULT_TEA_TIME_MINS = 5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            AppShortcutUtil.setupShortcuts(this);
        }

        if (AlarmHelper.isAlarmRunning()) {
            startActivity(new Intent(this, TeaTimeActivity.class));
        } else {
            startAlarm();
        }

        finish();
    }

    private void startAlarm() {
        int timerMins = DEFAULT_TEA_TIME_MINS;
        if (getIntent() != null) {
            timerMins = BundleUtil.getBundleValue(getIntent().getExtras(), AppShortcutUtil.KEY_TIME, DEFAULT_TEA_TIME_MINS);
        }

        AlarmHelper.startAlarm(this, timerMins, TeaTimeActivity.class);
    }

}
