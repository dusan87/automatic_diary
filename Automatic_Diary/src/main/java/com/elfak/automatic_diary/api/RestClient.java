package com.elfak.automatic_diary.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.client.*;
/**
 * Created by dusanristic on 12/4/13.
 */
public class RestClient {

    private static final String BASE_URL = "http://192.168.1.53:8000/";

    private static AsyncHttpClient client = new AsyncHttpClient();

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
