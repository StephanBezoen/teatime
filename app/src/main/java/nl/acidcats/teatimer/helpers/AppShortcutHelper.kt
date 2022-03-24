package nl.acidcats.teatimer.helpers

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import nl.acidcats.teatimer.R
import nl.acidcats.teatimer.ui.SplashActivity
import nl.acidcats.teatimer.util.IntentKey
import nl.acidcats.teatimer.util.putExtra

interface AppShortcutHelper {
    fun setupShortcuts()
}

class AppShortcutHelperImpl(
    private val context: Context,
    private val configHelper: ConfigHelper
) : AppShortcutHelper {

    override fun setupShortcuts() {
        val shortcuts = ArrayList<ShortcutInfo>()

        for (timeItem in configHelper.shortcutDurations) {
            val shortcutInfo = ShortcutInfo.Builder(context, context.getString(R.string.app_shortcut_id, timeItem))
                .setShortLabel(context.getString(R.string.app_shortcut_start_timer, timeItem))
                .setLongLabel(context.getString(R.string.app_shortcut_start_timer, timeItem))
                .setIcon(Icon.createWithResource(context, R.mipmap.ic_launcher))
                .setIntent(
                    Intent(context, SplashActivity::class.java)
                        .setAction(Intent.ACTION_VIEW)
                        .putExtra(IntentKey.Duration, timeItem)
                )
                .build()

            shortcuts.add(shortcutInfo)
        }

        val shortcutManager = context.getSystemService(ShortcutManager::class.java) ?: return
        shortcutManager.dynamicShortcuts = shortcuts
    }
}
