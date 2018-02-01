package nl.acidcats.teatimer.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import nl.acidcats.teatimer.R;
import nl.acidcats.teatimer.ui.SplashActivity;
import timber.log.Timber;

/**
 * Created on 01/02/2018.
 */

public class AppShortcutUtil {
    public static final String KEY_TIME = "key_time";

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)

    public static void setupShortcuts(Context context) {
        ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);
        if (shortcutManager == null) return;

        List<ShortcutInfo> shortcuts = new ArrayList<>();

        String[] timeList = context.getString(R.string.app_shortcut_times).split(",");
        for (String timeItem : timeList) {
            int timeInSec = Integer.parseInt(timeItem);

            ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(context, context.getString(R.string.app_shortcut_id, timeItem))
                    .setShortLabel(context.getString(R.string.app_shortcut_start_timer, timeItem))
                    .setLongLabel(context.getString(R.string.app_shortcut_start_timer, timeItem))
                    .setIcon(Icon.createWithResource(context, R.mipmap.ic_launcher))
                    .setIntent(new Intent(context, SplashActivity.class).setAction(Intent.ACTION_VIEW).putExtra(KEY_TIME, timeInSec))
                    .build();

            shortcuts.add(shortcutInfo);

            Timber.d("setupShortcuts: created shortcut for time %s", timeItem);
        }

        shortcutManager.setDynamicShortcuts(shortcuts);
    }
}
