package nl.acidcats.teatimer.helpers

import android.content.Context
import android.os.PowerManager
import kotlin.time.Duration

interface ScreenHelper {
    fun wakeScreen(duration: Duration)
}

@Suppress("DEPRECATION")
class ScreenHelperImpl(
    private val context: Context
) : ScreenHelper {

    override fun wakeScreen(duration: Duration) {
        (context.getSystemService(Context.POWER_SERVICE) as PowerManager)
            .newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, ScreenHelperImpl::class.java.canonicalName)
            .acquire(duration.inWholeMilliseconds)
    }
}
