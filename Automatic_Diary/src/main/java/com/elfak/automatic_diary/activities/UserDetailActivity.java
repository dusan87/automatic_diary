package com.elfak.automatic_diary.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.elfak.automatic_diary.R;
import com.elfak.automatic_diary.fragments.UserDetailFragment;

/**
 * Created by dusanristic on 12/26/13.
 */
public class UserDetailActivity extends FragmentActivity {

    Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        try{
            fragment = new UserDetailFragment();
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
