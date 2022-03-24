package nl.acidcats.teatimer.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import nl.acidcats.teatimer.helpers.WidgetHelper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Implementation of App Widget functionality.
 */
class TeaTimerWidget : AppWidgetProvider(), KoinComponent {

    private val widgetHelper: WidgetHelper by inject()

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            widgetHelper.updateWidget(appWidgetId, appWidgetManager)
        }
    }
}
