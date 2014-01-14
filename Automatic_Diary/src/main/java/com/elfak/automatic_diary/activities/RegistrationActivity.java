package com.elfak.automatic_diary.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.elfak.automatic_diary.R;
import com.elfak.automatic_diary.api.RestClient;
import com.elfak.automatic_diary.utils.NetUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;

import java.util.List;

/**
 * Created by dusanristic on 12/6/13.
 */
public class RegistrationActivity extends Activity {

    Button btn_register, btn_cancel;
    EditText edt_email, edt_password, edt_repassword;

    String email, password1, password2;

    ProgressDialog progressDialog;

    RestClient postHttpClient, getHttpClient;

    PersistentCookieStore cookieStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regestration);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // I made get request here in order to get all cookies that I need in rest of code
        getHttpClient = new RestClient();
        cookieStore = new PersistentCookieStore(RegistrationActivity.this);
        getHttpClient.setCookieStore(cookieStore);

        getHttpClient.get("check_user/", new AsyncHttpResponseHandler() {
        });
        bindElements();
    }

    private void bindElements() {

        //Text Editors
        edt_email = (EditText) findViewById(R.id.edt_registration_email);
        edt_password = (EditText) findViewById(R.id.edt_password);
        edt_repassword = (EditText) findViewById(R.id.edt_repassword);

        // Buttons
        btn_register = (Button) findViewById(R.id.btn_registration_next1);
        btn_cancel = (Button) findViewById(R.id.btn_cancel_registration);


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!NetUtils.isNetworkOnline(RegistrationActivity.this)) {
                    Toast.makeText(RegistrationActivity.this, "Device is not connected", Toast.LENGTH_SHORT).show();
                } else {
                    if ((edt_password.getText().toString().equals(edt_repassword.getText().toString()) || edt_password.getText().toString().equals(""))) {
                        checkUser();
                    } else if (edt_password.getText().toString() == "") {
                        Toast.makeText(RegistrationActivity.this, "Password field could not be blank!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Doesn't match passowrds, please correct it!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void checkUser() {

        progressDialog = ProgressDialog.show(RegistrationActivity.this, "Validation", "Checking data...", true);
        email = edt_email.getText().toString();
        password1 = edt_password.getText().toString();
        password2 = edt_repassword.getText().toString();

        postHttpClient = new RestClient();

        RequestParams params = new RequestParams();
        List<Cookie> cookies = cookieStore.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("csrftoken")) {
                params.put("csrfmiddlewaretoken", cookie.getValue());
            }
        }

        params.put("username", email);
        params.put("password1", password1);
        params.put("password2", password2);

        postHttpClient.post("check_user/", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                Intent i = new Intent(RegistrationActivity.this, RegistrationActivity_1.class);
                i.putExtra("username", email);
                i.putExtra("password1", password1);
                i.putExtra("password2", password2);
                startActivity(i);
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(RegistrationActivity.this, "User exist, please put another email", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                super.onFailure(statusCode, headers, responseBody, error);
            }
        });
    }

}
