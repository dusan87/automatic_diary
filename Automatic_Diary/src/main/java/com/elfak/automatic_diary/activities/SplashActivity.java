package com.elfak.automatic_diary.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.elfak.automatic_diary.R;

/**
 * Created by dusanristic on 1/17/14.
 */
public class SplashActivity extends Activity {

    Integer SPLASH_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        try {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (false) {
                        startMapActivity();
                        return;
                    } else {
                        startLoginActivity();
                        return;
                    }
                }
            }, SPLASH_DELAY);

            new Handler().postDelayed(new Runnable() {
                public void run() {
                }
            }, SPLASH_DELAY);
        } catch(Exception e){}
    }

    public void startMapActivity(){
        Intent intent = new Intent(SplashActivity.this, MapActivity.class);
        startActivity(intent);
        finish();
    }

    public void startLoginActivity(){
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
