package nl.acidcats.teatimer.util

import android.content.Context

data class AlarmState(
    val isAlarmRunning: Boolean,
    val alarmEndTime: Long
)

val StoppedAlarm = AlarmState(false, 0)

interface StorageHelper {
    var alarmState: AlarmState
}

class StorageHelperImpl(context: Context) : StorageHelper {

    private val preferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    override var alarmState: AlarmState
        get() = AlarmState(
            isAlarmRunning = preferences.getBoolean(PREFKEY_ALARM_RUNNING, false),
            alarmEndTime = preferences.getLong(PREFKEY_ALARM_END_TIME, 0L)
        )
        set(value) {
            preferences
                .edit()
                .putBoolean(PREFKEY_ALARM_RUNNING, value.isAlarmRunning)
                .putLong(PREFKEY_ALARM_END_TIME, value.alarmEndTime)
                .apply()
        }

    companion object {
        private const val PREFKEY_ALARM_RUNNING = "prefkey_alarmRunning"
        private const val PREFKEY_ALARM_END_TIME = "prefkey_alarmEndTime"
    }
}
