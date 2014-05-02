package com.elfak.automatic_diary.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by dusanristic on 5/2/14.
 */
public class BootReceiver extends BroadcastReceiver {

    LocationAlarmReceiver alarmReceiver = new LocationAlarmReceiver();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
            alarmReceiver.setAlarm(context);
    }
}
