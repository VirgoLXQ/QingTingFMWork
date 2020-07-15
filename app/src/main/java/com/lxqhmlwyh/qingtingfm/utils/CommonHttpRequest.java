package com.lxqhmlwyh.qingtingfm.utils;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommonHttpRequest {

    private static MediaType type=MediaType.parse("application/json;charset=utf-8");

    public static Response getHttp(String url){
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();
        try {
            Response response=okHttpClient.newCall(request).execute();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Response postHttp(String url, String json){
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody=RequestBody.create(type,json);
        Request request=new Request.Builder().url(url).patch(requestBody).build();
        try {
            Response response=okHttpClient.newCall(request).execute();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
