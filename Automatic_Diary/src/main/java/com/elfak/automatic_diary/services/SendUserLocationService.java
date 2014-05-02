package com.elfak.automatic_diary.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.elfak.automatic_diary.receivers.LocationAlarmReceiver;

/**
 * Created by dusanristic on 2/1/14.
 */
public class SendUserLocationService extends IntentService {


    public SendUserLocationService() {
        super("SendUserLocationService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i("Dusan", "service could be constructed");

        // Release the wake lock provided by the BroadcastReceiver.
        LocationAlarmReceiver.completeWakefulIntent(intent);

    }
}
