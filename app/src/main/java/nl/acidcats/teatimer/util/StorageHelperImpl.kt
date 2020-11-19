package nl.acidcats.teatimer.util

import android.content.Context

interface StorageHelper {
    var isAlarmRunning: Boolean

    var alarmEndTime: Long
}

class StorageHelperImpl(context: Context) : StorageHelper {

    private val preferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    companion object {
        private const val PREFKEY_ALARM_RUNNING = "prefkey_alarmRunning"
        private const val PREFKEY_ALARM_END_TIME = "prefkey_alarmEndTime"
    }

    override var isAlarmRunning: Boolean
        get() = preferences.getBoolean(PREFKEY_ALARM_RUNNING, false)
        set(value) {
            preferences
                    .edit()
                    .putBoolean(PREFKEY_ALARM_RUNNING, value)
                    .apply()
        }

    override var alarmEndTime: Long
        get() = preferences.getLong(PREFKEY_ALARM_END_TIME, 0L)
        set(value) {
            preferences
                    .edit()
                    .putLong(PREFKEY_ALARM_END_TIME, value)
                    .apply()
        }
}
