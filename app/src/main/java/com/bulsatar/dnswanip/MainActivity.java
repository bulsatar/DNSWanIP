package com.bulsatar.dnswanip;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;


public class MainActivity extends Activity implements  AdapterView.OnItemSelectedListener{ //View.OnClickListener,
    private static final String TAG = "waninfo";

    EditText metIP;
    EditText metPort;
    EditText metGenName;
    CheckBox mchkHTTPS;
    TextView mtvIP;



    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.server_info_input);

        //get views to easily set info
        metIP = (EditText) findViewById(R.id.etLink);
        metPort = (EditText) findViewById(R.id.etPort);
        metGenName = (EditText) findViewById(R.id.etGenName);
        mchkHTTPS = (CheckBox) findViewById(R.id.chkHTTPS);
        mtvIP = (TextView) findViewById(R.id.tvFullIP);
        //---------------------------------------------
        populateSpinner();

    }
    private  void populateSpinner(){
        //grab where we store the ip addresses and generic names and populate spinner selector

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Map<String, ?> ipList = mPrefs.getAll();
        ArrayList genNameList = new ArrayList();
        genNameList.add("Fill in for new or touch and select to edit");

        for (Map.Entry<String, ?> entry : ipList.entrySet()) {
            genNameList.add(entry.getKey());
        }

        //get choice selection
        Spinner sp = (Spinner) findViewById(R.id.spIPListMain);
        sp.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, genNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp.setAdapter(adapter);

        clearViews();

    }

    private void clearViews(){
        metGenName.setText("");
        metIP.setText("");
        metPort.setText("");
        mtvIP.setText("");
    }

    public void handlesCancel(View v){
        finish();
    }

    public  void handlesSave(View v){
        ProcessURL getDNSIP = new ProcessURL(this);
        getDNSIP.execute(metIP.getText().toString());


    }

    public void handlesDelete(View v){
        String tmpGenName = metGenName.getText().toString();
        if(tmpGenName != null && !tmpGenName.isEmpty() ){
            try{
                ManagePrefs mp = new ManagePrefs(this);
                mp.deleteSet(tmpGenName);
                populateSpinner();
            }catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
                String error = "Error attempting to delete item.  Please select item to delete again";
                Toast toast = Toast.makeText(this, error, Toast.LENGTH_LONG);
                toast.show();
            }

        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        clearViews();
        String sName = (String) parent.getItemAtPosition(position);
        if(sName != null && !sName .isEmpty() && !sName.equalsIgnoreCase("Fill in for new or touch and select to edit")) {
            ManagePrefs mp = new ManagePrefs(this);
            ManagePrefs.WanSet ws = mp.getPropsFromPrefs(sName);
            metIP.setText(ws.WanLink);
            metPort.setText(ws.PortNum);
            mchkHTTPS.setChecked(ws.UseHTTPS);
            metGenName.setText(ws.GenericName);
            mtvIP.setText(ws.FullIP);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //handled on save and delete
    }

    private class ProcessURL extends AsyncTask<String, Void, String> {

        Context mcontext;
        ProgressDialog pd;

        public ProcessURL(Context context){
            mcontext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(mcontext, "Wait", "Downloading...");
        }

        @Override
        protected String doInBackground(String... params) {
            return GET(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            pd.dismiss();
            //check for did not work
            if(result.equalsIgnoreCase("Did not work!")){
                String error = "Error retrieving wan IP.  Please ensure link only returns ip address (ie. 123.45.6.78)";
                Toast toast = Toast.makeText(mcontext, error, Toast.LENGTH_LONG);
                toast.show();
            }


            String http = "http://";
            if(mchkHTTPS.isChecked()) {
                http = "https://";
            }
            String port = "";
            if(!metPort.getText().toString().isEmpty()) {
                port = metPort.getText().toString();
            }

            ManagePrefs mp = new ManagePrefs(mcontext);
            String fullIP = http+result+":"+port;

            mp.setPrefsFromProps(metIP. getText().toString(), fullIP, port, metGenName.getText().toString(), mchkHTTPS.isChecked());

            populateSpinner();

            String error = "Information saved.  Full IP is \n" + fullIP + "\nand is now available to the widget";
            Toast toast = Toast.makeText(mcontext, error, Toast.LENGTH_LONG);
            toast.show();

        }



        private  String GET(String url){
            InputStream inputStream = null;
            String result = "Did not work!";

            //check if string is already ip address
            IPUtilities ipu = new IPUtilities();
            if(ipu.isIpAddress(url))
                return url;

            //check if connected since have to connect to get wan
            if(isConnected()) {

                try {

                    // create HttpClient
                    HttpClient httpclient = new DefaultHttpClient();

                    // make GET request to the given URL
                    HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

                    // receive response as inputStream
                    inputStream = httpResponse.getEntity().getContent();

                    // convert inputstream to string
                    if (inputStream != null) {
                        result = convertInputStreamToString(inputStream);
                    }
                } catch (Exception e) {
                    Log.d("InputStream", e.getLocalizedMessage());
                }
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
