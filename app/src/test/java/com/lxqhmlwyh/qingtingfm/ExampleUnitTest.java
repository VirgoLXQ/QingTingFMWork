package com.lxqhmlwyh.qingtingfm;



import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lxqhmlwyh.qingtingfm.pojo.FMCardViewJson;
import com.lxqhmlwyh.qingtingfm.utils.CommonHttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void newFmCardTest(){
        Response response=
        CommonHttpRequest.getHttp("https://rapi.qingting.fm/categories/239/channels?with_total=true&page=1&pagesize=4");
        ResponseBody responseBody=response.body();
        try {
            JSONObject result=new JSONObject(responseBody.string());
            JSONArray itemRoot=result.getJSONArray("items");
            Gson gson=new Gson();
            List<FMCardViewJson> newJson=
            gson.fromJson(itemRoot.toString(),new TypeToken<List<FMCardViewJson>>(){}.getType());
            for (FMCardViewJson json:newJson){
                System.out.println(json.getRegion().toString());
                System.out.println(json.getCity().toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}