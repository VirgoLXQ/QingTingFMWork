package com.lxqhmlwyh.qingtingfm;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lxqhmlwyh.qingtingfm.pojo.Broadcasters;
import com.lxqhmlwyh.qingtingfm.pojo.FMCardViewJson;
import com.lxqhmlwyh.qingtingfm.utils.CommonHttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.lxqhmlwyh.qingtingfm", appContext.getPackageName());
    }

    public void HTTPUtilsTest(){
        String url="http://api.avatardata.cn/MingRenMingYan/LookUp?" +
                "key=82d0485323844626b580dbc43be57fd7&keyword=天才&page="+5+"&rows=1";
        Response response=
        CommonHttpRequest.getHttp(url);
        ResponseBody responseBody=response.body();
        try {
            Log.e("CommonHttpUtilTest",responseBody.string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void newFmCardTest() {
        Log.e("newFmCardTest","start to test");
        Response response =
                CommonHttpRequest.getHttp("https://rapi.qingting.fm/categories/239/channels?with_total=true&page=1&pagesize=4");
        ResponseBody responseBody = response.body();
        try {
            JSONObject result = new JSONObject(responseBody.string());
            JSONObject data=result.getJSONObject("Data");
            JSONArray itemRoot = data.getJSONArray("items");
            Gson gson = new Gson();
            List<FMCardViewJson> newJson =
                    gson.fromJson(itemRoot.toString(), new TypeToken<List<FMCardViewJson>>() {
                    }.getType());
            for (FMCardViewJson json : newJson) {
                //System.out.println(json.getRegion().toString());
                //System.out.println(json.getCity().toString());
                Log.e("print region",json.getRegion().toString());
                Log.e("print city",json.getCity().toString());
                List<Broadcasters> broadcasters=json.getNowplaying().getBroadcasters();
                for (Broadcasters bro:broadcasters){
                    Log.e("print broadcasters",bro.toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}