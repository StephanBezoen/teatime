package nl.acidcats.teatimer.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import nl.acidcats.teatimer.R

/**
 * Utilities for notifications
 */

object NotificationUtil {

    fun showNotification(context: Context, id: Int, title: String, message: String, @StringRes channelIdId: Int, cls: Class<*>) {
        showNotification(context, id, title, message, false, channelIdId, cls)
    }

    fun showNotification(context: Context, id: Int, title: String, message: String, hiPrio: Boolean, @StringRes channelIdId: Int, cls: Class<*>) {
        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, context.getString(channelIdId))
            .setSmallIcon(R.drawable.ic_notification_teacup)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(PendingIntent.getActivity(context, 0, Intent(context, cls), PendingIntent.FLAG_UPDATE_CURRENT))

        if (hiPrio) {
            builder.setDefaults(NotificationCompat.DEFAULT_ALL).priority = NotificationCompat.PRIORITY_MAX
        }

        manager.notify(id, builder.build())
    }

    fun cancelNotification(context: Context, id: Int) {
        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        manager.cancel(id)
    }

    fun createNotificationChannel(context: Context,
                                  @StringRes channelIdId: Int, @StringRes nameId: Int, @StringRes descId: Int,
                                  importance: Int, enableVibration: Boolean, enableLights: Boolean) {
        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channelId = context.getString(channelIdId)
        val channelName = context.getString(nameId)
        val channelDescription = context.getString(descId)

        val channel = NotificationChannel(channelId, channelName, importance)
        channel.description = channelDescription
        channel.lightColor = Color.WHITE
        channel.enableLights(enableLights)
        channel.enableVibration(enableVibration)

        manager.createNotificationChannel(channel)
    }
}
