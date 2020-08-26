package com.lxqhmlwyh.qingtingfm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lxqhmlwyh.qingtingfm.R;
import com.lxqhmlwyh.qingtingfm.adapter.ProgramAdapter;
import com.lxqhmlwyh.qingtingfm.adapter.ProgramItemDecoration;
import com.lxqhmlwyh.qingtingfm.entities.ProgramItemEntity;
import com.lxqhmlwyh.qingtingfm.utils.CommonHttpRequest;
import com.lxqhmlwyh.qingtingfm.utils.MyTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class PlayListActivity extends AppCompatActivity {

    private TextView tvPrevious;
    private TextView tvChannel;
    private RecyclerView recyclerView;
    private List<ProgramItemEntity> entities;

    /**
     * 临时数据，方便Intent发送
     */
    public String cover;
    public String channelName;
    public int channelId;
    public int programId;
    public int count;
    public String startTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        initView();
        initData();
    }

    private void initView(){
        findViewById(R.id.play_list_back).setOnClickListener(onClickListener);
        tvPrevious=findViewById(R.id.play_list_previous);
        tvChannel=findViewById(R.id.play_list_channel_title);
        recyclerView=findViewById(R.id.play_list_program);
        tvChannel.setText(getIntent().getStringExtra("channel"));
        tvPrevious.setText(getIntent().getStringExtra("previous"));
    }

    private void initData(){
        startTime=getIntent().getStringExtra("startTime");
        cover=getIntent().getStringExtra("cover");
        count=getIntent().getIntExtra("count",0);
        programId=getIntent().getIntExtra("programId",0);
        channelName=getIntent().getStringExtra("channelName");
        channelId=getIntent().getIntExtra("channel_id",4875);
        final int dayOFWeek= MyTime.dayOFWeek();
        final String baseUrl="https://rapi.qingting.fm/v2/channels/"+channelId+"/playbills?day="+dayOFWeek;
        final AlertDialog loadingDialog=new AlertDialog.Builder(PlayListActivity.this).create();
        View loadView=View.inflate(this,R.layout.loading_dialog,null);
        loadingDialog.setView(loadView);
        loadingDialog.show();
        //获取电台数据
        CommonHttpRequest.getHttp(baseUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PlayListActivity.this, "获取电台数据失败", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) {
                ResponseBody responseBody= response.body();
                try {
                    JSONObject rootJson=new JSONObject(responseBody.string());
                    JSONObject dataObj=rootJson.getJSONObject("data");
                    JSONArray dayJson=dataObj.getJSONArray(""+dayOFWeek);
                    Gson gson=new Gson();
                    entities=
                            gson.fromJson(dayJson.toString(),new TypeToken<List<ProgramItemEntity>>(){}.getType());
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(() -> {
                    Toast.makeText(PlayListActivity.this, "获取电台数据成功", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                    showPlayList();
                });

            }
        });

    }

    private void showPlayList(){
        ProgramAdapter programAdapter=new ProgramAdapter(this,entities);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new ProgramItemDecoration());
        recyclerView.setAdapter(programAdapter);
    }

    private View.OnClickListener onClickListener= v -> {
        switch (v.getId()){
            case R.id.play_list_previous:
            case R.id.play_list_back:
                finish();
                break;
        }
    };
}
