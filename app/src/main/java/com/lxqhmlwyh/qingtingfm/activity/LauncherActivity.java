package com.lxqhmlwyh.qingtingfm.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lxqhmlwyh.qingtingfm.R;
import com.lxqhmlwyh.qingtingfm.service.InitDataService;
import com.lxqhmlwyh.qingtingfm.utils.CommonHttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class LauncherActivity extends AppCompatActivity {

    private TextView countDownText;//倒计时文本view
    private TextView sayingText;//显示名人名言
    private Intent intent;
    private int num=10;
    private static final int UPDATE_TEXT=2;
    private static final int COUNT_DOWN_END=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024,1024);//隐藏系统状态栏
        setContentView(R.layout.activity_launcher);
        initView();
        initData();
        getSaying();
        countDownTask();
    }

    /**
     * 初始化视图
     */
    private void initView(){
        //getSupportActionBar().hide();
        countDownText=findViewById(R.id.tv_launcher_step);
        sayingText=findViewById(R.id.tv_launcher_saying);
        //跳过倒计时
        countDownText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num=0;
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData(){
        intent=new Intent(LauncherActivity.this,MainActivity.class);
        startService(new Intent(this, InitDataService.class));
    }

    /**
     * 倒计时方法
     */
    private void countDownTask(){

        new Thread(new Runnable() {
            boolean isKeep=true;
            @Override
            public void run() {
                while (isKeep){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (num<=0){
                        handler.sendEmptyMessage(1);
                        isKeep=false;
                    }else{
                        num--;
                        handler.sendEmptyMessage(2);
                    }
                }
            }
        }).start();
    }


    /**
     * 从阿凡达获取数据
     */
    private void getSaying(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    int page=new Random().nextInt(10)+1;
                    ResponseBody responseBody= CommonHttpRequest.getHttp("http://api.avatardata.cn/MingRenMingYan/LookUp?" +
                            "key=82d0485323844626b580dbc43be57fd7&keyword=天才&page="+page+"&rows=1").body();
                    JSONObject jsonObject = new JSONObject(responseBody.string());
                    JSONArray resultArray=jsonObject.getJSONArray("result");
                    JSONObject resultObj=resultArray.getJSONObject(0);
                    String sayingStr=resultObj.getString("famous_saying")
                            +"——"+resultObj.getString("famous_name");

                    Message msg=new Message();
                    msg.what=3;
                    msg.obj=sayingStr;

                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 更新UI
     */
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case COUNT_DOWN_END:
                    startActivity(intent);
                    finish();
                    break;
                case UPDATE_TEXT:
                    countDownText.setText(num+"秒");
                    break;
                case 3:
                    sayingText.setText(msg.obj.toString());
                    break;
            }
        }
    };
}