package com.elfak.automatic_diary.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elfak.automatic_diary.R;
import com.elfak.automatic_diary.api.RestClient;
import com.elfak.automatic_diary.core.User;
import com.loopj.android.http.BinaryHttpResponseHandler;

import java.util.List;

/**
 * Created by dusanristic on 12/23/13.
 */
public class UserAdapter extends ArrayAdapter<User> {

    int resource;
    RestClient getHttpClientAvatar = new RestClient();
    public UserAdapter(Context context, int textViewResourceId, List<User> objects) {
        super(context, textViewResourceId, objects);
        this.resource = textViewResourceId;
    }


    @Override
    public User getItem(int position) {
        return super.getItem(position);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View rowView;
        if(view == null){
            String inflater = Context.LAYOUT_INFLATER_SERVICE;

            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(inflater);
            rowView = layoutInflater.inflate(R.layout.userlist_item, null);

        } else {
            rowView = view;
        }

        final ImageView avatar = (ImageView) rowView.findViewById(R.id.img_user_list);
        TextView full_name = (TextView) rowView.findViewById(R.id.tv_user_full_name);
        TextView country  = (TextView) rowView.findViewById(R.id.tv_user_country);
        TextView birth_day = (TextView) rowView.findViewById(R.id.tv_user_bday);
        full_name.setText(getItem(position).getFirstName()+ " " + getItem(position).getLastName());
        country.setText(getItem(position).getCurrentCountry() + ", " + getItem(position).getCurrentCity());
        birth_day.setText(getItem(position).getBday());
        String[] allowedContentTypes = new String[] { "image/png", "image/jpeg" };
        getHttpClientAvatar.get("media/" + getItem(position).getImageUrl(), new BinaryHttpResponseHandler(allowedContentTypes){
            @Override
            public void onSuccess(byte[] binaryData) {
                super.onSuccess(binaryData);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                options.inPurgeable = true;
                options.inSampleSize = 3;
                Bitmap icon  = BitmapFactory.decodeByteArray(binaryData,0, binaryData.length, options);
                avatar.setBackground(new BitmapDrawable(icon));
            }

        });

        return rowView;
    }
}
