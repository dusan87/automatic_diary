package com.elfak.automatic_diary.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import com.elfak.automatic_diary.R;

/**
 * Created by dusanristic on 12/16/13.
 */
public class UsersListActivity extends Activity{

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_users_list);

    }

}
