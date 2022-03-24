package nl.acidcats.teatimer.helpers

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

interface ToastHelper {
    fun showToast(@StringRes messageId: Int, duration: Int = Toast.LENGTH_SHORT)

    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT)
}

class ToastHelperImpl(
    private val context: Context
) : ToastHelper {

    override fun showToast(@StringRes messageId: Int, duration: Int) {
        Toast.makeText(context, messageId, duration).show()
    }

    override fun showToast(message: String, duration: Int) {
        Toast.makeText(context, message, duration).show()
    }
}
