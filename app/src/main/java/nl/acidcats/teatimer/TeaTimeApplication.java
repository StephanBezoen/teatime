package nl.acidcats.teatimer;

import android.app.Application;
import android.app.NotificationManager;
import android.os.Build;

import com.facebook.stetho.Stetho;
import com.pixplicity.easyprefs.library.Prefs;

import nl.acidcats.teatimer.util.NotificationUtil;
import timber.log.Timber;

/**
 * Application class for the TeaTimer application
 */

public class TeaTimeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Prefs.initPrefs(this);

        Timber.plant(new Timber.DebugTree());
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationUtil.createNotificationChannel(this,
                    R.string.notification_channel_id, R.string.notification_channel_name, R.string.notification_channel_description,
                    NotificationManager.IMPORTANCE_DEFAULT, false, false);

            NotificationUtil.createNotificationChannel(this,
                    R.string.alarm_channel_id, R.string.alarm_channel_name, R.string.alarm_channel_description,
                    NotificationManager.IMPORTANCE_HIGH, true, true);
        }
    }
}
