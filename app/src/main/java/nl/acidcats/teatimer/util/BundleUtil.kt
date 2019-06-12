package nl.acidcats.teatimer.util

import android.os.Bundle

/**
 * Created on 11/09/2017.
 */

@Suppress("UNCHECKED_CAST")
object BundleUtil {

    fun <T> getBundleValue(bundle: Bundle, key: String, defaultValue: T): T {
        val value = getBundleValue<T>(bundle, key)
        return value ?: defaultValue
    }

    private fun <T> getBundleValue(bundle: Bundle?, key: String): T? {
        if (bundle != null && bundle.containsKey(key)) {
            val value = bundle.get(key)
            if (value != null) {
                return value as T
            }
        }

        return null
    }
}
