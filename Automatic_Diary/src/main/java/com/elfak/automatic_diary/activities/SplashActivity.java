package com.elfak.automatic_diary.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.elfak.automatic_diary.R;
import com.elfak.automatic_diary.utils.SessionManager;

/**
 * Created by dusanristic on 1/17/14.
 */
public class SplashActivity extends Activity {

    Integer SPLASH_DELAY = 2000;

    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        session = new SessionManager(getApplicationContext());

        try {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (session.isLoggedIn()) {
                        startMainActivity();
                        return;
                    } else {
                        startLoginActivity();
                        return;
                    }
                }
            }, SPLASH_DELAY);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void startMainActivity(){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void startLoginActivity(){
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
