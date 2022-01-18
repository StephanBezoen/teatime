package nl.acidcats.teatimer.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.github.ajalt.timberkt.Timber
import nl.acidcats.teatimer.R
import nl.acidcats.teatimer.alarm.AlarmService
import kotlin.time.ExperimentalTime

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [TeaTimerWidgetConfigureActivity]
 */
@ExperimentalTime
class TeaTimerWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            deleteTitlePref(context, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

@ExperimentalTime
fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val views = RemoteViews(context.packageName, R.layout.tea_timer_widget)

    val serviceIntent = AlarmService.getAlarmServiceIntent(context, durationInMinutes = 5, appWidgetId = appWidgetId)
    val pendingIntent = PendingIntent.getForegroundService(context, appWidgetId, serviceIntent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
    views.setOnClickPendingIntent(R.id.teacupButton, pendingIntent)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
