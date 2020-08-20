package com.lxqhmlwyh.qingtingfm.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import com.lxqhmlwyh.qingtingfm.utils.CommonHttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 用于闪屏时下载数据
 */
public class InitDataService extends IntentService {

    //private static List<District> districts=new ArrayList<>();
    private static JSONArray district;//所有地区的json数据
    private static JSONArray categories;//电台类别json数据
    private static JSONObject userLocationInfo;//用户所在地json数据
    private static boolean INTERNET_ERROR=false;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     *
     */
    public InitDataService(String name) {
        super(name);
    }

    public InitDataService(){
        super("IntentService");
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
        //Log.e("InitDataService","开始下载数据");
    }

    /**
     * 闪屏时下载数据
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        try {
            Response response= CommonHttpRequest.getHttp("https://rapi.qingting.fm/regions");
            ResponseBody responseBody=response.body();

            Response response2= CommonHttpRequest.getHttp("https://rapi.qingting.fm/categories?type=channel");
            ResponseBody responseBody2=response2.body();

            Response response3= CommonHttpRequest.getHttp("https://ip.qingting.fm/ip");
            ResponseBody responseBody3=response3.body();

            JSONObject data=new JSONObject(responseBody.string());
            JSONArray array=data.getJSONArray("Data");
            district=array;
            //Log.e("district",district.toString());

            JSONObject data2=new JSONObject(responseBody2.string());
            JSONArray array2=data2.getJSONArray("Data");
            categories=array2;
            //Log.e("categories",categories.toString());

            JSONObject data3=new JSONObject(responseBody3.string());
            userLocationInfo=data3.getJSONObject("data");
            //Log.e("userLocationInfo",userLocationInfo.toString());

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            INTERNET_ERROR=true;
            Log.e("InitDataService","网络错误");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("InitDataService","初始化数据服务结束");
    }

    public static JSONArray getDistrict(){
        if (district!=null)return district;
        return null;
    }

    public static JSONArray getCategories() {
        if (categories!=null)
        return categories;
        return null;
    }

    public static JSONObject getUserLocationInfo() {
        if (userLocationInfo!=null)
        return userLocationInfo;
        return null;
    }
}
