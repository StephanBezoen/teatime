package nl.acidcats.teatimer.helpers

import android.content.Context
import nl.acidcats.teatimer.R
import nl.acidcats.teatimer.util.DurationUnit

interface ConfigHelper {
    val defaultDuration: Int
    val shortcutDurations: IntArray
    val durationUnit: DurationUnit
}

class ConfigHelperImpl(
    private val context: Context
) : ConfigHelper {
    override val defaultDuration: Int by lazy {
        context.resources.getInteger(R.integer.default_duration)
    }

    override val shortcutDurations: IntArray by lazy {
        context.resources.getIntArray(R.array.app_shortcut_times)
    }

    override val durationUnit: DurationUnit by lazy {
        DurationUnit.valueOf(context.getString(R.string.time_unit))
    }
}
