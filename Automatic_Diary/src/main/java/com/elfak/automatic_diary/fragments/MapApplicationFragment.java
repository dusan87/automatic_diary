package com.elfak.automatic_diary.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.elfak.automatic_diary.R;
import com.elfak.automatic_diary.api.RestClient;
import com.elfak.automatic_diary.utils.SessionManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
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
public class MapApplicationFragment extends SupportMapFragment {

    RestClient httpGetClient;
    GoogleMap googleMap;

    private static View view = null;

    SessionManager session;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        session = new SessionManager(activity.getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater,container,savedInstanceState);

        if(view != null){
            ViewGroup parent = (ViewGroup)view.getParent();
            if(parent !=null){
                parent.removeView(view);
            }
        }

        try {
            view = inflater.inflate(R.layout.map_activity, container ,false);
        } catch (InflateException e){
            e.printStackTrace();
        }

        if(googleMap == null)
             googleMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        getFriendsLocations();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void getFriendsLocations() {

        httpGetClient =  new RestClient();

        String username = session.getUserDetails().get(SessionManager.USERNAME);

        RequestParams params = new RequestParams("username", username);
        httpGetClient.get("friends_locations/", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject response) {
                Gson gson = new Gson();
                Map<String,Object> resultFullJsonArrayComplete = gson.fromJson(response.toString(), new TypeToken<Map<String,Object>>() {}.getType());

                List<LinkedTreeMap<String,String>> jsonOfResultsComplete;
                jsonOfResultsComplete = (List<LinkedTreeMap<String,String>>) resultFullJsonArrayComplete.get("results");

                if(jsonOfResultsComplete.size() > 0){
                    for(LinkedTreeMap<String,String> _friend : jsonOfResultsComplete){
                        addMarker(Double.parseDouble(_friend.get("lat")), Double.parseDouble(_friend.get("long")));
                    }
                }
                super.onSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity().getApplicationContext(), "FAILURE", Toast.LENGTH_LONG).show();
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
