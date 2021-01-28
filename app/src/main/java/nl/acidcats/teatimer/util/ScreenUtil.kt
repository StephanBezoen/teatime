package nl.acidcats.teatimer.util

import android.content.Context
import android.os.PowerManager
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@Suppress("DEPRECATION")
@ExperimentalTime
object ScreenUtil {

    fun wakeScreen(context: Context, duration: Duration) {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val lock = powerManager.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, ScreenUtil::class.java.canonicalName)
        lock.acquire(duration.toLongMilliseconds())
    }
}