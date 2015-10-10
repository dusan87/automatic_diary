package com.elfak.automatic_diary.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import org.apache.http.client.*;
/**
 * Created by dusanristic on 12/4/13.
 */
public class RestClient {

    private static final String BASE_URL = "http://10.0.3.2:8000/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public void setCookieStore(PersistentCookieStore cookieStore){
        client.setCookieStore(cookieStore);
    }

    public void post( String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public void get(String url, RequestParams params , JsonHttpResponseHandler jsonHttpResponseHandler){
        client.get(getAbsoluteUrl(url), params, jsonHttpResponseHandler);
    }

    public void get(String url, AsyncHttpResponseHandler responseHandler){
        client.get(getAbsoluteUrl(url),responseHandler);
    }

    public void get(String url, BinaryHttpResponseHandler binaryHttpResponseHandler){
        client.get(getAbsoluteUrl(url), binaryHttpResponseHandler);
    }

    public void setBasicAuth(String username, String password) {
        client.setBasicAuth(username, password);
    }

    public void setHeader(String header, String value){
        client.addHeader(header, value);
    }
    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
