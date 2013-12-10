package com.elfak.automatic_diary.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by dusanristic on 12/4/13.
 * This calss is made in order to check connection of devices
 */


public class NetUtils {

    public static boolean isNetworkOnline(Context context){
        try{
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if(networkInfo != null  && networkInfo.isConnected()){
                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
