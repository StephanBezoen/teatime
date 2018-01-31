package nl.acidcats;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.pixplicity.easyprefs.library.Prefs;

import nl.acidcats.teatimer.BuildConfig;
import nl.acidcats.teatimer.R;
import nl.acidcats.util.NotificationUtil;
import timber.log.Timber;

/**
 * Application class for the TeaTimer application
 */

public class TeaTimeApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        Prefs.initPrefs(this);

        Timber.plant(new Timber.DebugTree());
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }

        NotificationUtil.createNotificationChannel(this,
                R.string.notification_channel_id, R.string.notification_channel_name, R.string.notification_channel_description);
    }
}
