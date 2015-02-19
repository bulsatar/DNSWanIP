package com.bulsatar.dnswanip;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.content.Context.WIFI_SERVICE;


/**
 * Implementation of App Widget functionality.
 */
public class SingleClickWanIP extends AppWidgetProvider {
    private static final String TAG = "waninfo";

    private String myURL;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.d(TAG,"started update: " + myURL);
        WifiManager wifiManager = (WifiManager)  context.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        String homessid = "\"Soun-Router\"";

        if (ssid.equalsIgnoreCase(homessid)){
            myURL = "192.168.1.106";
        }
        Log.d(TAG,"finished ssd check: " + myURL);
        String newURL = "http://"+myURL+":8080";
        Uri uri = Uri.parse(newURL);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.single_click_wan_ip);
        Log.d(TAG,"finished intent and remote views: " + newURL);
        views.setOnClickPendingIntent(R.id.ibOrange, pendingIntent);
        Log.d(TAG,"finished on click pending: " + newURL);

        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
        Log.d(TAG,"finished onUpdate: " + newURL);
    }



    @Override
    public void onEnabled(Context context) {
        Log.d(TAG,"onEnabled: " + myURL);
        ProcessURL process = new ProcessURL(context);
        process.execute("https://www.dropbox.com/s/hg0b12h4yo7yh6x/wanip.txt?raw=1");
        Log.d(TAG,"started process: " + myURL);

    }



    private class ProcessURL extends AsyncTask<String, Void, String> {

        private Context gContext;
        public ProcessURL(Context context) {
            this.gContext = context;
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG,"started background");
            return GET(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            myURL = result;

            AppWidgetManager widgetManager = AppWidgetManager.getInstance(gContext);
            ComponentName widgetComponent = new ComponentName(gContext, SingleClickWanIP.class);
            int[] widgetIds = widgetManager.getAppWidgetIds(widgetComponent);
            Intent update = new Intent();
            update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
            update.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            gContext.sendBroadcast(update);
            Log.d(TAG,"finished and set myURL variable: " + myURL);
        }



        private  String GET(String url){
            InputStream inputStream = null;
            String result = "";
            try {

                // create HttpClient
                HttpClient httpclient = new DefaultHttpClient();

                // make GET request to the given URL
                HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

                // receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

                // convert inputstream to string
                if(inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";

            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }

            return result;
        }

        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;

        }
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


