package com.bulsatar.dnswanip;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;



public class MainActivity extends ActionBarActivity {
    private static final String TAG = "waninfo";

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tvHTMLDisplay);
    }



    public  void GetHTML(View view) {
        if(isConnected()){
            ProcessURL process = new ProcessURL();
            Intent processIntent = new Intent(this,ProcessURL.class);

            startActivityForResult(processIntent,1);

            String myURL = process.MyURL;
            tv.setText(process.MyURL);
            Uri uri = Uri.parse(myURL);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    //

    //private class HttpAsyncTask extends AsyncTask<String, Void, String> {

    //@Override

    //protected String doInBackground(String... urls) {

    //

    //return GET(urls[0]);

    //}

    //// onPostExecute displays the results of the AsyncTask.

    //@Override

    //protected void onPostExecute(String result) {

    //WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

    //WifiInfo wifiInfo = wifiManager.getConnectionInfo();

    //String ssid = wifiInfo.getSSID();

    //String homessid = "\"Soun-Router\"";

    //

    //if (ssid.equalsIgnoreCase(homessid)){

    //result = "192.168.1.106";

    //}

    //String newURL = "http://"+result+":8080";

    //tv.setText(newURL);

    //Uri uri = Uri.parse(newURL);

    //Intent intent = new Intent(Intent.ACTION_VIEW, uri);

    //startActivity(intent);

    //}

    //}

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
