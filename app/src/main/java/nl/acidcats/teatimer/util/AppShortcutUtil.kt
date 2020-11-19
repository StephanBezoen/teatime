package nl.acidcats.teatimer.util

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import com.github.ajalt.timberkt.Timber
import nl.acidcats.teatimer.R
import nl.acidcats.teatimer.ui.SplashActivity
import java.util.*

/**
 * Created on 01/02/2018.
 */

object AppShortcutUtil {
    const val KEY_TIME = "key_time"

    fun setupShortcuts(context: Context) {
        val shortcuts = ArrayList<ShortcutInfo>()

        val timeList = context.getString(R.string.app_shortcut_times).split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (timeItem in timeList) {
            val timeInSec = Integer.parseInt(timeItem)

            val shortcutInfo = ShortcutInfo.Builder(context, context.getString(R.string.app_shortcut_id, timeItem))
                    .setShortLabel(context.getString(R.string.app_shortcut_start_timer, timeItem))
                    .setLongLabel(context.getString(R.string.app_shortcut_start_timer, timeItem))
                    .setIcon(Icon.createWithResource(context, R.mipmap.ic_launcher))
                    .setIntent(Intent(context, SplashActivity::class.java).setAction(Intent.ACTION_VIEW).putExtra(KEY_TIME, timeInSec))
                    .build()

            shortcuts.add(shortcutInfo)

            Timber.d { "setupShortcuts: created shortcut for time $timeItem" }
        }

        val shortcutManager = context.getSystemService(ShortcutManager::class.java) ?: return
        shortcutManager.dynamicShortcuts = shortcuts
    }
}
