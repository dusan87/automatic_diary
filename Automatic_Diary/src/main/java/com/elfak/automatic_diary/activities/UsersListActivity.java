package com.elfak.automatic_diary.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.elfak.automatic_diary.R;
import com.elfak.automatic_diary.adapters.UserAdapter;
import com.elfak.automatic_diary.api.RestClient;
import com.elfak.automatic_diary.core.User;
import com.elfak.automatic_diary.fragments.UsersListFragment;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dusanristic on 12/16/13.
 */
public class UsersListActivity extends Activity{

    RestClient getHttpClient;

    private UserAdapter aa;
    private ArrayList<User> users;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_users_list);

        RequestParams params = new RequestParams();
        String username = getIntent().getStringExtra("username");
        params.put("username", username);
        // get reference to the Fragment
        users =  new ArrayList<User>();

        getHttpClient = new RestClient();
        getHttpClient.get("all_users/", params,  new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                Gson gson =  new Gson();
                Map<String,Object> resultFullJsonArrayComplete =  gson.fromJson(response.toString(), new TypeToken<Map<String,Object>>() {}.getType());
                Toast.makeText(UsersListActivity.this, "SUCCESS", Toast.LENGTH_LONG).show();
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
                    users.add(0,user);

                }
                aa.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Throwable e, JSONObject errorResponse) {
                super.onFailure(statusCode, e, errorResponse);
                Toast.makeText(UsersListActivity.this, "ERORRRRR", Toast.LENGTH_LONG).show();
            }

        });

        FragmentManager fragmentManager = getFragmentManager();
        UsersListFragment usersListFragment = (UsersListFragment) fragmentManager.findFragmentById(R.id.usersListFragment);

        aa = new UserAdapter(UsersListActivity.this, R.layout.userlist_item, users);

        if (usersListFragment != null) usersListFragment.setListAdapter(aa);

    }

}
