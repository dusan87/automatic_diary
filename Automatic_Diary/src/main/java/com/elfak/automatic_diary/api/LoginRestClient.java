package com.elfak.automatic_diary.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.client.*;
/**
 * Created by dusanristic on 12/4/13.
 */
public class LoginRestClient {

    private static final String BASE_URL = "127.0.0.1:8000/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    private static AsyncHttpClient getClient(){
        return client;
    }
    public void setCookieStore(CookieStore cookieStore){
        client.setCookieStore(cookieStore);
    }

    public void post( String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public void get(String url, AsyncHttpResponseHandler responseHandler){
        client.get(getAbsoluteUrl(url),responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}