package com.bulsatar.dnswanip;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.widget.RemoteViews;


/**
 * Implementation of App Widget functionality.
 */
public class SingleClickWanIP extends AppWidgetProvider {

    private String myURL;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        Uri uri = Uri.parse(myURL);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.single_click_wan_ip);
        views.setOnClickPendingIntent(R.id.ibOrange, pendingIntent);

        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }


    @Override
    public void onEnabled(Context context) {
        ProcessURL process = new ProcessURL();
        process.execute("https://www.dropbox.com/s/hg0b12h4yo7yh6x/wanip.txt?raw=1");
        String myURL = process.MyURL;
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        //
        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        //// Construct the RemoteViews object
        //RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.single_click_wan_ip);
        //views.setTextViewText(R.id.appwidget_text, widgetText);
        //
        //// Instruct the widget manager to update the widget
        //appWidgetManager.updateAppWidget(appWidgetId, views);
        //
    }



}


