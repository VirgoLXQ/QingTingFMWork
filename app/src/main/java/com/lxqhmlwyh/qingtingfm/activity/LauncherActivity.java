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
import android.widget.Toast;

import com.lxqhmlwyh.qingtingfm.R;
import com.lxqhmlwyh.qingtingfm.databaseutil.APPVisitTable;
import com.lxqhmlwyh.qingtingfm.service.InitDataService;
import com.lxqhmlwyh.qingtingfm.utils.CommonHttpRequest;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

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
        updateVisit();
        initView();
        initData();
        getSaying();
        countDownTask();
    }

    /**
     * 记录和更新app的访问次数
     */
    private void updateVisit(){
        Iterator tables= APPVisitTable.findAll(APPVisitTable.class);
        long nowTimeStamp=System.currentTimeMillis();//获取当前的时间戳
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date(nowTimeStamp));
        int nowYear=calendar.get(Calendar.YEAR);
        int nowMoth=calendar.get(Calendar.MONTH)+1;
        int nowDay=calendar.get(Calendar.DAY_OF_MONTH);

        while(tables.hasNext()){
            APPVisitTable tableObj=(APPVisitTable) tables.next();
            Log.e("updateVisit",tableObj.toString());
            long thisTimeStamp=tableObj.getTimeStamp();//获取数据库的时间戳
            Calendar thisCalendar=Calendar.getInstance();
            thisCalendar.setTime(new Date(thisTimeStamp));
            int thisYear=calendar.get(Calendar.YEAR);
            int thisMoth=calendar.get(Calendar.MONTH)+1;
            int thisDay=calendar.get(Calendar.DAY_OF_MONTH);
            if (thisDay==nowDay&&thisMoth==nowMoth&&thisYear==nowYear){//如果是同一天
                Log.e("updateVisit","更新今天的访问次数");
                int count=tableObj.getCount()+1;
                SugarRecord sugarRecord=tableObj;
                long id=sugarRecord.getId();
                SugarRecord.executeQuery("update APP_VISIT_TABLE set count=? where id=?",count+"",id+"");
                return;
            }else{
                continue;
            }
        }

        APPVisitTable newData=new APPVisitTable();
        newData.setCount(1);
        newData.setTimeStamp(nowTimeStamp);
        newData.save();
        Log.e("updateVisit",newData.toString());
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
                    Response response= CommonHttpRequest.getHttp("http://api.avatardata.cn/MingRenMingYan/LookUp?" +
                            "key=82d0485323844626b580dbc43be57fd7&keyword=天才&page="+page+"&rows=1");
                    if (response==null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LauncherActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                    ResponseBody responseBody=response.body();
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