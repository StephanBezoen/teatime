package nl.acidcats.teatimer.util

import android.app.Notification
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

    fun showNotification(context: Context, id: Int, title: String, message: String, isImportant: Boolean, @StringRes channelIdId: Int, cls: Class<*>) {
        val notification = createNotification(context, channelIdId, title, message, cls, isImportant)

        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(id, notification)
    }

    private fun createNotification(
        context: Context,
        channelIdId: Int,
        title: String,
        message: String,
        cls: Class<*>,
        isImportant: Boolean
    ): Notification {
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

        val builder = NotificationCompat.Builder(context, context.getString(channelIdId))
            .setSmallIcon(R.drawable.ic_notification_teacup)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(PendingIntent.getActivity(context, 0, Intent(context, cls), flags))

        if (isImportant) {
            builder.setDefaults(NotificationCompat.DEFAULT_ALL).priority = NotificationCompat.PRIORITY_MAX
            builder.setCategory(Notification.CATEGORY_ALARM)
            builder.setTimeoutAfter(5000)
        } else {
            builder.setOngoing(true)
            builder.setUsesChronometer(true)
            builder.setCategory(Notification.CATEGORY_SERVICE)
        }

        return builder.build()
    }

    fun cancelNotification(context: Context, id: Int) {
        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        manager.cancel(id)
    }

    fun createNotificationChannel(
        context: Context,
        @StringRes channelIdId: Int,
        @StringRes nameId: Int,
        @StringRes descId: Int,
        isImportant: Boolean,
        enableVibration: Boolean,
        enableLights: Boolean
    ) {
        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channelId = context.getString(channelIdId)
        val channelName = context.getString(nameId)
        val channelDescription = context.getString(descId)

        val importance = if (isImportant) NotificationManager.IMPORTANCE_HIGH else NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(channelId, channelName, importance)
        channel.description = channelDescription
        channel.lightColor = Color.WHITE
        channel.enableLights(enableLights)

        if (enableVibration) {
            channel.vibrationPattern = longArrayOf(500, 500, 500, 500)
        }

        manager.createNotificationChannel(channel)
    }
}
