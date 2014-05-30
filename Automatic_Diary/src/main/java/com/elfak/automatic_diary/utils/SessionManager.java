package com.elfak.automatic_diary.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.elfak.automatic_diary.activities.LoginActivity;

import java.util.HashMap;

/**
 * Created by dusanristic on 5/29/14.
 */
public class SessionManager {

    SharedPreferences pref;

    Editor editor;

    Context context;

    int PRIVATE_MODE = 0;

    //shared pref file name
    private static final String PREF_NAME = "AutomaticDiaryPref";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String USERNAME = "email";

    public SessionManager(Context context) {

        this.context = context;
        pref = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String username){

        editor.putBoolean(IS_LOGIN,true);

        editor.putString(USERNAME, username);

        editor.commit();
    }

    public HashMap<String,String> getUserDetails(){

        HashMap<String,String> user = new HashMap<String, String>();

        user.put(USERNAME,pref.getString(USERNAME, null));

        return user;
    }

    public void logoutUser(){

        editor.clear();
        editor.commit();

        Intent i = new Intent(this.context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        this.context.startActivity(i);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
