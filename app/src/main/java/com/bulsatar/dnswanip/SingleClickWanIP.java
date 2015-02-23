package com.bulsatar.dnswanip;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


/**
 * Implementation of App Widget functionality.
 */
public class SingleClickWanIP extends AppWidgetProvider {

    private static final String TAG = "waninfo";
    private String myURL;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.single_click_wan_ip);

        Intent intent = new Intent(context,IPSelection.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.ibOrange, pendingIntent);

        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            appWidgetManager.updateAppWidget(appWidgetIds[i],views);
        }


    }

    @Override
    public void onEnabled(Context context) {    }



    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

//        CharSequence widgetText = context.getString(R.string.appwidget_text);
//        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.single_click_wan_ip);
//        views.setTextViewText(R.id.ibOrange, widgetText);
//
//        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);

    }



}


