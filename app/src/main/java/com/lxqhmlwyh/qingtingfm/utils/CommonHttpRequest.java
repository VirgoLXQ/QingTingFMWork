package com.lxqhmlwyh.qingtingfm.utils;


import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 通用的Http请求工具
 */
public class CommonHttpRequest {

    private static MediaType type=MediaType.parse("application/json;charset=utf-8");

    /**
     * 发起get请求
     */
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

    /**
     * 发起get请求
     * @param url
     * @param callback
     */
    public static void getHttp(String url, Callback callback){
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 发起post请求
     * @param url
     * @param json
     * @return
     */
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
