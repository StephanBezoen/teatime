package nl.acidcats.teatimer.helpers

import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.RemoteViews
import nl.acidcats.teatimer.R
import nl.acidcats.teatimer.TimerActionReceiver

interface WidgetHelper {
    fun updateWidget(
        appWidgetId: Int = 0,
        appWidgetManager: AppWidgetManager? = null,
    )
}

class WidgetHelperImpl(
    private val context: Context
) : WidgetHelper {
    override fun updateWidget(appWidgetId: Int, appWidgetManager: AppWidgetManager?) {
        if (appWidgetId == 0) return

        val views = RemoteViews(context.packageName, R.layout.tea_timer_widget)

        val pendingIntent = TimerActionReceiver.getStartPendingIntent(
            context = context,
            widgetId = appWidgetId
        )
        views.setOnClickPendingIntent(R.id.teacupButton, pendingIntent)

        val manager = appWidgetManager ?: AppWidgetManager.getInstance(context)
        manager.updateAppWidget(appWidgetId, views)

    }

}