package com.lxqhmlwyh.qingtingfm.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.lxqhmlwyh.qingtingfm.R;


public class LauncherActivity extends AppCompatActivity {

    private TextView countDownText;//倒计时文本view
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
        countDownTask();
    }

    /**
     * 初始化视图
     */
    private void initView(){
        //getSupportActionBar().hide();
        countDownText=findViewById(R.id.tv_launcher_step);

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
            }
        }
    };
}