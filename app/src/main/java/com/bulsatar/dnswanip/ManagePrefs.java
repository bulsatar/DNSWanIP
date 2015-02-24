package com.bulsatar.dnswanip;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.HashSet;
import java.util.Map;

/**
 * Created by bulsatar on 2/23/15.
 */
public class ManagePrefs {

    private static final String TAG = "waninfo";
    Context mcontext;

    public ManagePrefs(Context pcontext){
        mcontext = pcontext;
    }

    public class WanSet{
        public String WanLink;
        public String FullIP;
        public String PortNum;
        public String GenericName;
        public Boolean UseHTTPS;
    }

    public WanSet getPropsFromPrefs(String pGenName){
        Log.d(TAG, "start get props");
        WanSet rtnset = new WanSet();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(mcontext);
        Log.d(TAG, "got shared props");
        Map<String, ?> ipList = mPrefs.getAll();
        Log.d(TAG, "set to map");

        HashSet<String> tmpHash = (HashSet) ipList.get(pGenName);
        Log.d(TAG, "got hashset from map");
        for(String tmpstr : tmpHash){
            Log.d(TAG, "temp string -- "+ tmpstr);

            if(tmpstr.substring(0,"url:".length()).equalsIgnoreCase("url:")){
                rtnset.FullIP = tmpstr.substring("url:".length(),tmpstr.length());
                Log.d(TAG, "set fullip -- "+ rtnset.FullIP);
            }
            if(tmpstr.substring(0,"link:".length()).equalsIgnoreCase("link:")){
                rtnset.WanLink = tmpstr.substring("link:".length(),tmpstr.length());
                Log.d(TAG, "set link -- "+ rtnset.WanLink);
            }
            if(tmpstr.substring(0,"port:".length()).equalsIgnoreCase("port:")){
                rtnset.PortNum = tmpstr.substring("port:".length(),tmpstr.length());
                Log.d(TAG, "set port -- "+ rtnset.PortNum);
            }
            if(tmpstr.substring(0,"https:".length()).equalsIgnoreCase("https:")){
                rtnset.UseHTTPS = Boolean.valueOf(tmpstr.substring("https:".length(), tmpstr.length()));
                Log.d(TAG, "set https -- "+ rtnset.UseHTTPS);
            }
        }
        rtnset.GenericName = pGenName;
        return rtnset;
    }

    public String getFullIP(String pGenName){
        WanSet tmpset = getPropsFromPrefs(pGenName);
        return tmpset.FullIP;
    }

    public void setPrefsFromProps(String pLink,String pFullIP, String pPort, String pGenName, Boolean pHTTPS){
        HashSet<String> prefsHash = new HashSet<>();
        prefsHash.add("url:"+ pFullIP);
        prefsHash.add("link:"+ pLink);
        prefsHash.add("port:"+ pPort);
        prefsHash.add("https:"+ pHTTPS.toString());

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(mcontext);
        SharedPreferences.Editor edit=mPrefs.edit();

        edit.putStringSet(pGenName,prefsHash);
        edit.commit();

    }

    public void deleteSet(String pGenName){
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(mcontext);
        SharedPreferences.Editor edit=mPrefs.edit();

        edit.remove(pGenName);
        edit.commit();
    }

}
