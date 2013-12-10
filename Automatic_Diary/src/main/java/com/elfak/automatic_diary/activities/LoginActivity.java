package com.elfak.automatic_diary.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.elfak.automatic_diary.R;
import com.elfak.automatic_diary.api.LoginRestClient;
import com.elfak.automatic_diary.utils.NetUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;

import java.util.List;

/**adm
 * Created by dusanristic on 11/29/13.
 */



public class LoginActivity extends Activity{

    Button sign_in, sing_up;
    EditText edt_username, edt_password;

    String username, password;

    LoginRestClient client ;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);
        setContentView(R.layout.activity_login);
        bindElements();


    }

    private void bindElements(){

        edt_username = (EditText) findViewById(R.id.edt_login_username);
        edt_username.setText(username);

        edt_password = (EditText) findViewById(R.id.edt_login_pass);
        edt_password.setText(password);

        sign_in = (Button) findViewById(R.id.btn_signin);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!NetUtils.isNetworkOnline(LoginActivity.this)){
                    Toast.makeText(LoginActivity.this, "Device is not connected. Please, connect device and try again to Login!", 10).show();
                }else{
                    attemptLogin();
                }
            }


        });

        sing_up = (Button) findViewById(R.id.btn_signup);

        sing_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!NetUtils.isNetworkOnline(LoginActivity.this)){
                    Toast.makeText(LoginActivity.this, "Device is not connected", Toast.LENGTH_LONG).show();
                }else {
                    Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                    startActivity(i);
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void attemptLogin() {

        progressDialog = ProgressDialog.show(LoginActivity.this, "Signing in","Verifying account.", true);
        // Get value from email edit_text field
        username = edt_username.getText().toString();
        password = edt_password.getText().toString();

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(LoginActivity.this);
        client.setCookieStore(myCookieStore);

        List<Cookie> cookies = myCookieStore.getCookies();

        for(Cookie cookie : cookies){
            if(cookie.getName().equals("csrftoken")){
                params.put("csrfmiddlewaretoken", cookie.getValue());
            }
        }

        params.put("username", username);
        params.put("password", password);

        client.post("http://192.168.1.53:8000/login_user/", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                Toast.makeText(LoginActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
                Toast.makeText(LoginActivity.this, "FAILURE", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });



    }


}
