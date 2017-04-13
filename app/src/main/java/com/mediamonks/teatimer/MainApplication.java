package com.mediamonks.teatimer;

import android.app.Application;

import com.pixplicity.easyprefs.library.Prefs;

/**
 * Created by stephan on 23/03/2017.
 */

public class MainApplication extends Application{
    private static final String TAG = MainApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        Prefs.initPrefs(this);
    }
}
