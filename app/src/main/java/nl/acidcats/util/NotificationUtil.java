package nl.acidcats.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationCompat;

import nl.acidcats.teatimer.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Utilities for notifications
 */

public class NotificationUtil {

    public static void showNotification(Context context, int id, String title, String message, @StringRes int channelIdId, Class<?> cls) {
        showNotification(context, id, title, message, false, channelIdId, cls);
    }

    public static void showNotification(Context context,
                                        int id, String title, String message, boolean hiPrio, @StringRes int channelIdId, Class<?> cls) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (manager == null) return;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(channelIdId))
                .setSmallIcon(R.drawable.ic_notification_teacup)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, cls), PendingIntent.FLAG_UPDATE_CURRENT));

        if (hiPrio) {
            builder.setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_MAX);

        }

        manager.notify(id, builder.build());
    }

    public static void cancelNotification(Context context, int id) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (manager == null) return;

        manager.cancel(id);
    }

    public static void createNotificationChannel(Context context, @StringRes int channelIdId, @StringRes int nameId, @StringRes int descId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return;

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (manager == null) return;

        String channelId = context.getString(channelIdId);
        String channelName = context.getString(nameId);
        String channelDescription = context.getString(descId);

        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(channelDescription);
        channel.enableLights(true);
        channel.setLightColor(Color.WHITE);
        channel.enableVibration(true);

        manager.createNotificationChannel(channel);
    }
}
