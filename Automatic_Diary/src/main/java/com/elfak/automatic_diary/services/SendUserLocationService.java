package com.elfak.automatic_diary.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.elfak.automatic_diary.R;
import com.elfak.automatic_diary.activities.LoginActivity;
import com.elfak.automatic_diary.activities.MapActivity;
import com.elfak.automatic_diary.api.RestClient;
import com.elfak.automatic_diary.receivers.LocationAlarmReceiver;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.cookie.Cookie;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by dusanristic on 2/1/14.
 */
public class SendUserLocationService extends Service {


    PersistentCookieStore cookieStore;
    RestClient postHttpClient = new RestClient();
    private Location location;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    private static boolean completedService = false;
    private final int NOTIFICATION_ID = 1;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        cookieStore = new PersistentCookieStore(getBaseContext());
        postHttpClient = new RestClient();
        location = getUserLocation();
        Log.i("Login", Double.toString(location.getLongitude()));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Dusan", "Started Intent");

        if(location != null){

            RequestParams params = new RequestParams();

            List<Cookie> cookies = cookieStore.getCookies();

            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("csrftoken")) {
                    params.put("csrfmiddlewaretoken", cookie.getValue());
                }
            }
            params.put("lat", Double.toString(location.getLatitude()));
            params.put("long", Double.toString(location.getLongitude()));
            params.put("username", LoginActivity.user.getUsername());

            Log.i("Login", LoginActivity.user.getUsername());
            postHttpClient.post("update_location/", params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(JSONObject response) {
                    completedService = true;

                    Gson gson = new Gson();
                    Map<String,Object> resultFullJsonArrayComplete = gson.fromJson(response.toString(), new TypeToken<Map<String,Object>>() {}.getType());

                    List<LinkedTreeMap<String,String>> jsonOfResultsComplete;
                    jsonOfResultsComplete = (List<LinkedTreeMap<String,String>>) resultFullJsonArrayComplete.get("results");

                    Log.i("SendUserLocationService", Integer.toString(jsonOfResultsComplete.size()));
                    if(jsonOfResultsComplete.size() > 0)
                        for (LinkedTreeMap<String,String> _friend : jsonOfResultsComplete)
                        {
                            if(_friend.get("type").contentEquals("together")){
                                Log.i("Together","Together");
                            } else
                                raiseNotification(jsonOfResultsComplete.size());
                        }
                    super.onSuccess(response);
                }
            });

            if(completedService){
                Log.i("Login","Completed");
                LocationAlarmReceiver.completeWakefulIntent(intent);
            }

        }
        return super.onStartCommand(intent, flags, startId);

    }


    private Location getUserLocation(){

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        String provider = locationManager.getBestProvider(criteria, true);

        Location location = locationManager.getLastKnownLocation(provider);


        if(location!=null){
            return location;
        }

        return null;
    }

    //    public SendUserLocationService() {
//        super("SendUserLocationService");
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//
//        Log.i("Dusan", "service could be constructed");
//
//        mNotificationManager = (NotificationManager)
//                this.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, MapActivity.class), 0);
//
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.ic_launcher)
//                        .setContentTitle("Service msg")
//                        .setStyle(new NotificationCompat.BigTextStyle()
//                                .bigText("You have got the alarm message"))
//                        .setContentText("You have got the alarm message");
//
//        mBuilder.setContentIntent(contentIntent);
//
//        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
//
//        // Release the wake lock provided by the BroadcastReceiver.
//        LocationAlarmReceiver.completeWakefulIntent(intent);
//
//    }

    public void raiseNotification(Integer friends_number){

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this , 0, new Intent(this, MapActivity.class) , 0);

        String contextText = "You have got " + friends_number.toString()  + (friends_number > 1 ? " friends" : " friend") +  " near by you!";
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Service msg")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Keep in touch with friends"))
                        .setContentText(contextText);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }
}
