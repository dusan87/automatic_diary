package com.elfak.automatic_diary;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.res.Configuration;
import android.os.Build;

/**
 * Created by dusanristic on 11/14/13.
 */
public class MyApplication extends Application {

    private static MyApplication singleton;

    //Returns the application instance

    public static MyApplication getInstace(){
        return  singleton;
    }

    @Override
    public final void onCreate() {
        super.onCreate();
        singleton = this;
    }

    @Override
    public final void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
