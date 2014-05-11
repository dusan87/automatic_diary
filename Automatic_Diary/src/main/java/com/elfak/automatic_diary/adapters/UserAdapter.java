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
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elfak.automatic_diary.R;
import com.elfak.automatic_diary.api.RestClient;
import com.elfak.automatic_diary.core.User;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dusanristic on 12/23/13.
 */
public class UserAdapter extends BaseAdapter {

    private final List<User> mUsers = new ArrayList<User>();

    private final Context mContext;

    private RestClient getHttpClientAvatar = new RestClient();

    public UserAdapter(Context context) {
        mContext = context;
    }

    // Add an User to the adapter

    public  void add(User user){
        mUsers.add(user);
        notifyDataSetChanged();
    }

    public void clear(){
        mUsers.clear();
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return mUsers.get(position);
    }

    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final User user = (User)getItem(position);

        View rowView;
        if(view == null){
            String inflater = Context.LAYOUT_INFLATER_SERVICE;

            LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(inflater);
            rowView = layoutInflater.inflate(R.layout.userlist_item, null);

        } else {
            rowView = view;
        }

        final SmartImageView avatar = (SmartImageView) rowView.findViewById(R.id.img_user_list);
        TextView full_name = (TextView) rowView.findViewById(R.id.tv_user_full_name);
        TextView country  = (TextView) rowView.findViewById(R.id.tv_user_country);
        TextView birth_day = (TextView) rowView.findViewById(R.id.tv_user_bday);

        full_name.setText(user.getFirstName()+ " " + user.getLastName());
        country.setText(user.getCurrentCountry() + ", " + user.getCurrentCity());
        birth_day.setText(user.getBday());

        String[] allowedContentTypes = new String[] { "image/png", "image/jpeg" };
        getHttpClientAvatar.get("media/" + user.getImageUrl(), new BinaryHttpResponseHandler(allowedContentTypes){
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

//        SmartImage smartImage = new SmartImage() {
//            @Override
//            public Bitmap getBitmap(Context context) {
//                context.getContentResolver();
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                options.inPurgeable = true;
//                options.inSampleSize = 3;
//
//            }
//        }
//        String  imgUrl = getHttpClientAvatar.getAbsoluteUrl("media/" + user.getImageUrl());


        return rowView;
    }
}
