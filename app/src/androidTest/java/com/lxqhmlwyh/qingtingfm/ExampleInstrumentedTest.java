package com.lxqhmlwyh.qingtingfm;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.lxqhmlwyh.qingtingfm.utils.CommonHttpRequest;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

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
}