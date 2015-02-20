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
    private Context gContext;


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.single_click_wan_ip);


        if(myURL != null) {
            Log.d(TAG, "started update: " + myURL);

            Uri uri = Uri.parse(myURL);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            Log.d(TAG, "finished intent and remote views: " + myURL);
            views.setOnClickPendingIntent(R.id.ibOrange, pendingIntent);
            Log.d(TAG, "finished on click pending: " + myURL);
        }
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            appWidgetManager.updateAppWidget(appWidgetIds[i],views);
        }




    }
//    todo: move dropbox link to preferences.  save urls there also, one away and one home
//    that way can move the async off of the widget and just force open the app and reset settings

    //todo: include catches for not having defined url

    @Override
    public void onEnabled(Context context) {
        gContext = context;
        ProcessURL process = new ProcessURL(context,AppWidgetManager.getInstance(context));
        process.execute("https://www.dropbox.com/s/hg0b12h4yo7yh6x/wanip.txt?raw=1");


    }

    private class ProcessURL extends AsyncTask<String, Void, String> {

        private Context gContext;
        private AppWidgetManager gappWidgetManager;
        private RemoteViews views;

        public ProcessURL(Context context, AppWidgetManager pappWidgetManager) {
            this.gContext = context;
            this.gappWidgetManager = pappWidgetManager;
            views = new RemoteViews(gContext.getPackageName(), R.layout.single_click_wan_ip);
        }

        @Override
        protected String doInBackground(String... params) {
            return GET(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            WifiManager wifiManager = (WifiManager) gContext.getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();
            String homessid = "\"Soun-Router\"";

            if (ssid.equalsIgnoreCase(homessid)) {
                result = "192.168.1.106";
            }

            myURL = "http://" + result + ":8080";

            Uri uri = Uri.parse(myURL);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            PendingIntent pendingIntent = PendingIntent.getActivity(gContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            RemoteViews views = new RemoteViews(gContext.getPackageName(), R.layout.single_click_wan_ip);

            views.setOnClickPendingIntent(R.id.ibOrange, pendingIntent);


            ComponentName thisWidget = new ComponentName(gContext, SingleClickWanIP.class);
            gappWidgetManager.updateAppWidget(thisWidget, views);


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

//        CharSequence widgetText = context.getString(R.string.appwidget_text);
//        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.single_click_wan_ip);
//        views.setTextViewText(R.id.ibOrange, widgetText);
//
//        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);

    }



}


