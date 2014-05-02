package com.elfak.automatic_diary.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

import com.elfak.automatic_diary.services.SendUserLocationService;

/**
 * Created by dusanristic on 5/2/14.
 */
public class LocationAlarmReceiver extends WakefulBroadcastReceiver {

    private static final long INITAL_ALARM_DELAY = 2 * 60 * 1000L;
    //App's alarm menager that provides access to the system alarm service
    private AlarmManager mAlarmMng;

    private PendingIntent alarmIntent;

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent serviceIntent = new Intent(context, SendUserLocationService.class);


        startWakefulService(context, serviceIntent);
    }

    public void setAlarm(Context context) {

        mAlarmMng = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, LocationAlarmReceiver.class);

        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        mAlarmMng.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + INITAL_ALARM_DELAY,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);

        // enable to automatically restart the alarm when device is rebooted.

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Toast.makeText(context, "ALARM IS SET", Toast.LENGTH_LONG).show();

    }

    public void cancelAlarm(Context context) {

        if (mAlarmMng != null) {
            mAlarmMng.cancel(alarmIntent);
        }

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

    }
}
