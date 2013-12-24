package com.elfak.automatic_diary.fragments;

import android.annotation.TargetApi;
import android.app.ListFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.elfak.automatic_diary.core.User;

/**
 * Created by dusanristic on 12/16/13.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class UsersListFragment extends ListFragment  {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        User clickedUser = (User) getListView().getItemAtPosition(position);
        Toast.makeText(getActivity(), "DUsANNNN", Toast.LENGTH_LONG).show();
    }
}
