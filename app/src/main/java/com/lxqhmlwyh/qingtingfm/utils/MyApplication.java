package com.lxqhmlwyh.qingtingfm.utils;

import android.app.Application;
import android.content.Context;

import com.orm.SugarContext;

public class MyApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
