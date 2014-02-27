package com.elfak.automatic_diary.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.elfak.automatic_diary.R;
import com.elfak.automatic_diary.activities.MapActivity;
import com.elfak.automatic_diary.activities.UserDetailActivity;
import com.elfak.automatic_diary.adapters.UserAdapter;
import com.elfak.automatic_diary.api.RestClient;
import com.elfak.automatic_diary.core.User;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by dusanristic on 12/16/13.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class UsersListFragment extends ListFragment  {

    private UserAdapter mAdapter;

    RestClient getHttpClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter =  new UserAdapter(getActivity());

        RequestParams params = new RequestParams();
        String username = getActivity().getIntent().getStringExtra("username");
        params.put("username", username);

        getHttpClient = new RestClient();
        getHttpClient.get("all_users/", params,  new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                Gson gson =  new Gson();
                Map<String,Object> resultFullJsonArrayComplete =  gson.fromJson(response.toString(), new TypeToken<Map<String,Object>>() {}.getType());
                Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_LONG).show();
                List<LinkedTreeMap<String,String>> jsonOfResultsComplete = (List<LinkedTreeMap<String,String>>)resultFullJsonArrayComplete.get("results");

                for(LinkedTreeMap<String, String> _user : jsonOfResultsComplete){
                    User user = new User();
                    user.setFirstName(_user.get("first_name"));
                    user.setLastName(_user.get("last_name"));
                    user.setCurrentCountry(_user.get("country"));
                    user.setUsername(_user.get("username"));
                    user.setImage(_user.get("image"));
                    user.setCurrentCity(_user.get("city"));
                    user.setBirthDay(_user.get("birth_day"));
                    user.setGender(_user.get("gender"));
                    mAdapter.add(user);

                }
            }

            @Override
            public void onFailure(int statusCode, Throwable e, JSONObject errorResponse) {
                super.onFailure(statusCode, e, errorResponse);
                Toast.makeText(getActivity(), "ERORRRRR", Toast.LENGTH_LONG).show();
            }

        });

        setListAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // Send avatar through intent
        ImageView avatar = (ImageView) v.findViewById(R.id.img_user_list);
        avatar.buildDrawingCache();
        Bitmap icon = avatar.getDrawingCache();
        Bundle extras = new Bundle();
        extras.putParcelable("icon_bitmap", icon);

        User clickedUser = (User) getListView().getItemAtPosition(position);
        Intent intent = new Intent(getActivity().getBaseContext(), UserDetailActivity.class);
        intent.putExtra("name", clickedUser.getFirstName() + " " + clickedUser.getLastName());
        intent.putExtra("location", clickedUser.getCurrentCountry() + ", " + clickedUser.getCurrentCity());
        intent.putExtra("birth_day", clickedUser.getBday());
        intent.putExtra("username", clickedUser.getUsername());
        intent.putExtra("gender", clickedUser.getGender());
        intent.putExtra("logged_username", getActivity().getIntent().getStringExtra("username"));
        intent.putExtras(extras);
        getActivity().startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.user_list_skip_action, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.ac_skip:

                Intent intent = new Intent(getActivity().getBaseContext(), MapActivity.class);
                getActivity().startActivity(intent);

            default:

                return super.onOptionsItemSelected(item);
        }
    }
}
