package com.elfak.automatic_diary.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.elfak.automatic_diary.R;
import com.elfak.automatic_diary.activities.UsersListActivity;
import com.elfak.automatic_diary.api.RestClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;

import java.util.List;

/**
 * Created by dusanristic on 12/26/13.
 */

public class UserDetailFragment extends Fragment {

    private View mainView;
    TextView tv_user_detail_name, tv_user_detail_username;
    TextView tv_user_detail_sex, tv_user_detail_current_location, tv_user_detail_birth_day;
    Button btn_user_detail_cancel, btn_user_detail_follow;
    ImageView iv_avatar;

    RestClient httpClientPost;

    PersistentCookieStore cookieStore;

    String user_name = " ";

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        cookieStore = new PersistentCookieStore(getActivity().getBaseContext());

        mainView = inflater.inflate(R.layout.fragment_user_detail, container, false);

        iv_avatar = (ImageView)mainView.findViewById(R.id.iv_user_detail_photo);

        tv_user_detail_name = (TextView)mainView.findViewById(R.id.tv_user_detail_name);
        tv_user_detail_username = (TextView) mainView.findViewById(R.id.tv_user_detail_username);
        tv_user_detail_current_location = (TextView) mainView.findViewById(R.id.tv_user_detail_current_location);
        tv_user_detail_sex = (TextView) mainView.findViewById(R.id.tv_user_detail_sex);
        tv_user_detail_birth_day = (TextView) mainView.findViewById(R.id.tv_user_detail_birth_day);

        btn_user_detail_cancel = (Button) mainView.findViewById(R.id.btn_user_detail_cancel);
        btn_user_detail_follow = (Button) mainView.findViewById(R.id.btn_user_detail_follow);

        if(getActivity().getIntent().getExtras().containsKey("name")){
            tv_user_detail_name.setText(getArguments().get("name").toString());
        }
        if(getActivity().getIntent().getExtras().containsKey("location")){
            tv_user_detail_current_location.setText(getArguments().get("location").toString());
        }

        if(getActivity().getIntent().getExtras().containsKey("birth_day")){
            tv_user_detail_birth_day.setText(getArguments().get("birth_day").toString());
        }

        if(getActivity().getIntent().getExtras().containsKey("username")){
            tv_user_detail_username.setText(getArguments().get("username").toString());
        }

        if(getActivity().getIntent().getExtras().containsKey("gender")){
            if(getArguments().get("gender").toString().equals("M")){
                tv_user_detail_sex.setText("Male");
            } else {
                tv_user_detail_sex.setText("Female");
            }
        }

        if(getActivity().getIntent().getExtras().containsKey("icon_bitmap")){
            Bitmap avatar = getActivity().getIntent().getExtras().getParcelable("icon_bitmap");
            iv_avatar.setBackground(new BitmapDrawable(avatar));
        }

        btn_user_detail_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                httpClientPost = new RestClient();
                RequestParams params = new RequestParams();

                List<Cookie> cookies = cookieStore.getCookies();

                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("csrftoken")) {
                        params.put("csrfmiddlewaretoken", cookie.getValue());
                    }
                }

                user_name = getArguments().get("logged_username").toString();
                params.put("friend_username", getArguments().get("username").toString());
                params.put("logged_username", getArguments().get("logged_username").toString());

                httpClientPost.post("add_follower/", params, new AsyncHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        super.onSuccess(statusCode, headers, responseBody);
                        Intent intent = new Intent(getActivity().getBaseContext(), UsersListActivity.class);
                        intent.putExtra("username",user_name);
                        getActivity().startActivity(intent);
                    }
                });
            }
        });


        getActivity().getActionBar().setDisplayShowCustomEnabled(true);
        return mainView;
    }
}
