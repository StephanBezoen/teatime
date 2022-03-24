package nl.acidcats.teatimer.util

import android.content.Intent

enum class IntentKey {
    Action,
    Duration,
    WidgetId
}
enum class IntentAction(val value: String, val code:Int) {
    StartAction("start", 1),
    CancelAction("cancel", 2),
    DoneAction("done", 3);

    companion object {
        fun fromValue(value: String) = values().first { it.value == value }
    }
}

fun Intent.putExtra(key: IntentKey, value: Int?): Intent = apply {
    value?.let { putExtra(key.name, it) }
}

fun Intent?.getIntExtra(key: IntentKey, defaultValue: Int = 0): Int =
    this?.getIntExtra(key.name, defaultValue) ?: defaultValue

fun Intent.setAction(action: IntentAction): Intent {
    return putExtra(IntentKey.Action.name, action.value)
}

fun Intent?.getAction(): IntentAction? {
    val value = this?.getStringExtra(IntentKey.Action.name) ?: return null
    return IntentAction.fromValue(value)
}
