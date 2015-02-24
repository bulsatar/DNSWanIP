package com.bulsatar.dnswanip;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by bulsatar on 2/22/15.
 */
public class IPSelection extends Activity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        //grab where we store the ip addresses and generic names and populate spinner selector
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        Map<String, ?> ipList = mPrefs.getAll();
        ArrayList genNameList = new ArrayList();
        genNameList.add("Choose IP");
        for (Map.Entry<String, ?> entry : ipList.entrySet()) {
            genNameList.add(entry.getKey());
        }

        //start dialog creation
        setContentView(R.layout.file_setup);

        //get choice selection
        Spinner sp = (Spinner) findViewById(R.id.spIPList);
        sp.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, genNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp.setAdapter(adapter);



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String tmpName = (String) parent.getItemAtPosition(position);

        if(tmpName != null && !tmpName .isEmpty() && !tmpName.equalsIgnoreCase("Choose IP")) {
            ManagePrefs mp = new ManagePrefs(this);
            String newURL = mp.getFullIP(tmpName);
            Uri uri = Uri.parse(newURL);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

            finish();
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        finish();

    }


}
