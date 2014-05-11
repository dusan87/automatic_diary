package com.elfak.automatic_diary.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.elfak.automatic_diary.R;
import com.elfak.automatic_diary.api.RestClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by dusanristic on 1/15/14.
 */
public class MapActivity extends Activity {

    RestClient httpGetClient;
    GoogleMap googleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        getFriendsLocations();
    }

    private void getFriendsLocations() {

        httpGetClient =  new RestClient();

        RequestParams params = new RequestParams("username", LoginActivity.user.getUsername());
        httpGetClient.get("friends_locations/", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject response) {
                Gson gson = new Gson();
                Map<String,Object> resultFullJsonArrayComplete = gson.fromJson(response.toString(), new TypeToken<Map<String,Object>>() {}.getType());

                List<LinkedTreeMap<String,String>> jsonOfResultsComplete;
                jsonOfResultsComplete = (List<LinkedTreeMap<String,String>>) resultFullJsonArrayComplete.get("results");

                if(jsonOfResultsComplete.size() > 0){
                    for(LinkedTreeMap<String,String> _friend : jsonOfResultsComplete){
                        Log.i("MapActivity", (_friend.get("lat")) + "/" +  _friend.get("long"));
                        addMarker(Double.parseDouble(_friend.get("lat")), Double.parseDouble(_friend.get("long")));
                    }
                }
                super.onSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MapActivity.this, "FAILURE", Toast.LENGTH_LONG).show();
                super.onFailure(statusCode, headers, responseBody, error);
            }
        });
    }

    private void addMarker(Double lat, Double lon) {
        LatLng location = new LatLng(lat,lon);
        Marker friendLocation = googleMap.addMarker(new MarkerOptions()
                        .position(location)
        );
    }
}
