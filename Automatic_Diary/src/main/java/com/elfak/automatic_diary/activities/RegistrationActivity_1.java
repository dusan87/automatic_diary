package com.elfak.automatic_diary.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.elfak.automatic_diary.R;
import com.elfak.automatic_diary.api.RestClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by dusanristic on 12/10/13.
 */
public class RegistrationActivity_1 extends Activity {

    String username, password1, passowrd2, first_name, last_name, gender, bday, country, city;
    EditText edt_first_name, edt_last_name, edt_country, edt_city;
    Spinner sp_gender;
    TextView tv_bday_date;
    ImageView iv_profile_photo;

    Button btn_cancel, btn_next;

    Uri imageUri;

    ProgressDialog progressDialog;

    PersistentCookieStore cookieStore;

    RestClient getHttpClient, postHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1_registration);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getHttpClient = new RestClient();
        cookieStore = new PersistentCookieStore(RegistrationActivity_1.this);
        getHttpClient.setCookieStore(cookieStore);
        getHttpClient.get("create_user/", new AsyncHttpResponseHandler() {
        });

        bindElements();
    }


    private void bindElements() {

        edt_first_name = (EditText) findViewById(R.id.edt_name);
        edt_last_name = (EditText) findViewById(R.id.edt_last_name);
        edt_country = (EditText) findViewById(R.id.edt_country);
        edt_city = (EditText) findViewById(R.id.edt_city);
        tv_bday_date = (TextView) findViewById(R.id.tv_registration_bday);
        sp_gender = (Spinner) findViewById(R.id.sp_registration_gender);

        // Buttons

        btn_next = (Button) findViewById(R.id.btn_registration_next1);
        btn_cancel = (Button) findViewById(R.id.btn_cancel_registration);

        // Get Intent from Registration activity

        Intent intent = getIntent();

        username = intent.getStringExtra("username");
        password1 = intent.getStringExtra("password1");
        passowrd2 = intent.getStringExtra("password2");


        // Profile photo /Image view

        iv_profile_photo = (ImageView) findViewById(R.id.iv_profile_photo);

        iv_profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create Dialog in order to select type of taking photo Upload/Take
                AlertDialog.Builder alertDialogUploadTakePhoto = new AlertDialog.Builder(RegistrationActivity_1.this);
                alertDialogUploadTakePhoto.setTitle("Profile photo");
                alertDialogUploadTakePhoto.setItems(new CharSequence[]{"Take photo", "Upload photo"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                takePhoto();
                                break;
                            case 1:
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                final int ACTIVITY_SELECT_IMAGE = 1234;
                                startActivityForResult(intent, ACTIVITY_SELECT_IMAGE);
                                break;
                        }
                    }
                });
                alertDialogUploadTakePhoto.create().show();
            }
        });

        // initialization date picker
        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                tv_bday_date.setText(String.valueOf(year) + "-" + String.valueOf(monthOfYear) + "-" + String.valueOf(dayOfMonth));
            }
        };

        tv_bday_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(RegistrationActivity_1.this, datePickerListener, 2013, 03, 05);
                dialog.show();
            }
        });


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillUserProfile();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    private void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File folder = new File(Environment.getExternalStorageDirectory() + "/DictionaryTemp");


        if (!folder.exists()) {
            if (folder.mkdir()) {
                System.out.println("The dir is created successful");
            } else {
                System.out.println("There is a problem with creation directory");
            }
        }

        File photo = new File(Environment.getExternalStorageDirectory(), "/DictionaryTemp/profile.png");


        imageUri = Uri.fromFile(photo);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        final int ACTIVITY_TAKE_PHOTO = 12345;
        startActivityForResult(intent, ACTIVITY_TAKE_PHOTO);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1234:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap image = BitmapFactory.decodeFile(filePath);
                    Drawable drawable = new BitmapDrawable(getResources(), image);
                    iv_profile_photo.setBackground(drawable);
                }
                break;
            case 12345:
                if (resultCode == RESULT_OK) {

                    Bitmap image = getProfilePhoto();
                    Drawable profile_photo = new BitmapDrawable(getResources(), image);
                    iv_profile_photo.setBackground(profile_photo);
                }
                break;
        }
    }

    private Bitmap getProfilePhoto() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inPurgeable = true;
        options.inSampleSize = 3;
        Bitmap photo = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/DictionaryTemp/profile.png", options);

        return photo;
    }

    private void fillUserProfile() {

        progressDialog = ProgressDialog.show(RegistrationActivity_1.this, "Create account", "Creating account ...", true);
        first_name = edt_first_name.getText().toString();
        last_name = edt_last_name.getText().toString();
        gender = sp_gender.getSelectedItem().toString();
        bday = tv_bday_date.getText().toString();
        city = edt_city.getText().toString();
        country = edt_country.getText().toString();

        postHttpClient = new RestClient();

        RequestParams params = new RequestParams();

        List<Cookie> cookies = cookieStore.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("csrftoken")) {
                params.put("csrfmiddlewaretoken", cookie.getValue());
            }
        }

        File profile_photo = new File(Environment.getExternalStorageDirectory() + "/DictionaryTemp/profile.png");

        try {
            params.put("image", profile_photo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        params.put("first_name", first_name);
        params.put("last_name", last_name);
        params.put("username", username);
        params.put("password1", password1);
        params.put("password2", passowrd2);
        params.put("city", city);
        params.put("country", country);
        params.put("birth_day", bday);


        postHttpClient.post("create_user/", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                Intent intent = new Intent(RegistrationActivity_1.this, LoginActivity.class);
                startActivity(intent);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
                Toast.makeText(RegistrationActivity_1.this, "Failure", Toast.LENGTH_LONG).show();
            }

        });


    }


}
