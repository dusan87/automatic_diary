package com.elfak.automatic_diary.fragments;

import android.annotation.TargetApi;
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

import com.elfak.automatic_diary.R;
import com.elfak.automatic_diary.activities.MapActivity;
import com.elfak.automatic_diary.activities.UserDetailActivity;
import com.elfak.automatic_diary.core.User;

/**
 * Created by dusanristic on 12/16/13.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class UsersListFragment extends ListFragment  {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
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
