package com.elfak.automatic_diary.activities;

import android.app.Activity;
import android.os.Bundle;

import com.elfak.automatic_diary.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

/**
 * Created by dusanristic on 1/15/14.
 */
public class MapActivity extends Activity {

    GoogleMap googleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
    }
}
