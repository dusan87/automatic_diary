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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.Random;

import com.elfak.automatic_diary.utils.Constants;

/**
 * Created by dusanristic on 12/10/13.
 */
public class RegistrationActivity_1 extends Activity {

    String username, password1, passowrd2, first_name, last_name, bday, country, city, phone;
    EditText edt_first_name, edt_last_name, edt_country, edt_city, edt_phone, edt_birth_day;

    ImageView iv_profile_photo;
    RadioGroup rg_gender;

    Button btn_cancel, btn_next;

    Uri imageUri;
    ProgressDialog progressDialog;
    PersistentCookieStore cookieStore;
    RestClient getHttpClient, postHttpClient;

    boolean image_flag = false;
    String profile_photo_path = null;
    String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1_registration);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        cookieStore = new PersistentCookieStore(RegistrationActivity_1.this);


        bindElements();
    }


    private void bindElements() {

        edt_first_name = (EditText) findViewById(R.id.edt_name);
        edt_last_name = (EditText) findViewById(R.id.edt_last_name);
        edt_country = (EditText) findViewById(R.id.edt_country);
        edt_city = (EditText) findViewById(R.id.edt_city);
        edt_birth_day = (EditText) findViewById(R.id.edt_bday);
        rg_gender = (RadioGroup) findViewById(R.id.rg_gender);
        edt_phone = (EditText) findViewById(R.id.edt_phone);


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

                edt_birth_day.setText(String.valueOf(year) + "-" + String.valueOf(monthOfYear) + "-" + String.valueOf(dayOfMonth));
            }
        };

        edt_birth_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(RegistrationActivity_1.this, datePickerListener, 2013, 03, 05);
                dialog.show();
            }
        });


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {

                    fillUserProfile();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        // Set default gender value
        setDefaultGender();
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
                    image_flag = true;
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    profile_photo_path = filePath;
                    Bitmap image = BitmapFactory.decodeFile(filePath);
                    Drawable drawable = new BitmapDrawable(getResources(), image);
                    iv_profile_photo.setBackground(drawable);
                }
                break;
            case 12345:
                if (resultCode == RESULT_OK) {
                    image_flag = false;
                    profile_photo_path = null;
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
        bday = edt_birth_day.getText().toString();
        city = edt_city.getText().toString();
        country = edt_country.getText().toString();
        phone = edt_phone.getText().toString();

        postHttpClient = new RestClient();
        RequestParams params = new RequestParams();

        File profile_photo = image_flag ?new File(profile_photo_path) : new File(Environment.getExternalStorageDirectory() + "/DictionaryTemp/profile.png");

        try {
            params.put(Constants.IMAGE, profile_photo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        params.put(Constants.USER_FIRST_NAME, first_name);
        params.put(Constants.USER_LAST_NAME, last_name);
        params.put(Constants.USER_EMAIL, username);
        params.put(Constants.USER_PASSWORD, password1);
        params.put(Constants.USER_BIRTHDAY, bday);
        params.put(Constants.USER_GENDER, gender);
        params.put(Constants.CITY, city);
        params.put(Constants.COUNTRY, country);
        params.put(Constants.PHONE, phone);

        postHttpClient.post(Constants.API_CREATE_USER, params, new AsyncHttpResponseHandler() {

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
                progressDialog.dismiss();
            }

        });


    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.rb_male:
                if (checked) gender = Constants.USER_GENDER_MALE;
            case R.id.rb_female:
                if (checked) gender = Constants.USER_GENDER_FEMALE;
        }
    }

    private boolean validateFields() {
        if (edt_first_name.getText().toString().trim().equals("")) {
            edt_first_name.setError(Constants.REQUIRED_FIELD);

            return false;
        }

        if (edt_last_name.getText().toString().trim().equals("")) {
            edt_last_name.setError(Constants.REQUIRED_FIELD);

            return false;
        }

        if (edt_country.getText().toString().trim().equals("")) {
            edt_country.setError(Constants.REQUIRED_FIELD);
            return false;
        }

        if (edt_city.getText().toString().trim().equals("")) {
            edt_city.setError(Constants.REQUIRED_FIELD);
            return false;
        }

        if (edt_phone.getText().toString().trim().equals("")) {
            edt_phone.setError(Constants.REQUIRED_FIELD);
            return false;
        }

        if (edt_birth_day.getText().toString().trim().equals("")) {
            edt_birth_day.setError(Constants.REQUIRED_FIELD);
            return false;
        }

        return true;

    }

    private void setDefaultGender(){
        rg_gender.check(R.id.rb_male);
        gender = Constants.USER_GENDER_MALE;
    }
}
