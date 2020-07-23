package com.lxqhmlwyh.qingtingfm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.lxqhmlwyh.qingtingfm.R;

public class PlayActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView fmCoverImg;
    private TextView titleView;
    private TextView broadcasterView;
    private TextView startTimeView;
    private TextView endTimeView;
    private SeekBar seekBar;
    private static String channelName;
    private static String coverUrl;
    private static String title;
    private static String broadcaster;
    private static String START_TIME;
    private static String END_TIME;
    private static int DURATION;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        initData();
        initView();
    }
    private void initView(){
        toolbar=findViewById(R.id.play_toolbar);
        fmCoverImg=findViewById(R.id.play_cover);
        titleView=findViewById(R.id.play_title_view);
        broadcasterView=findViewById(R.id.play_broadcaster_view);
        startTimeView=findViewById(R.id.play_start_time);
        endTimeView=findViewById(R.id.play_end_time);
        startTimeView.setText(START_TIME);
        endTimeView.setText(END_TIME);
        titleView.setText(title);
        broadcasterView.setText(broadcaster);
        seekBar=findViewById(R.id.play_seek_bar);
        seekBar.setProgress(DURATION);
        toolbar.setNavigationIcon(R.mipmap.back_32);
        toolbar.setTitle(channelName);
        Glide.with(this).load(coverUrl)
                .error(R.mipmap.default_album).into(fmCoverImg);
        toolbar.setTitleTextColor(getResources().getColor(R.color.font_white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initData(){
        coverUrl=getIntent().getStringExtra("cover")==null?coverUrl:getIntent().getStringExtra("cover");
        channelName=getIntent().getStringExtra("channelName")==null?channelName:getIntent().getStringExtra("channelName");
        broadcaster=getIntent().getStringExtra("broadcaster")==null?broadcaster:getIntent().getStringExtra("broadcaster");
        title=getIntent().getStringExtra("title")==null?title:getIntent().getStringExtra("title");
        START_TIME=getIntent().getStringExtra("start_time")==null?START_TIME:getIntent().getStringExtra("start_time");
        END_TIME=getIntent().getStringExtra("end_time")==null?END_TIME:getIntent().getStringExtra("end_time");
        DURATION=getIntent().getIntExtra("duration",0)==0?DURATION:
                getIntent().getIntExtra("duration",0)/60;
    }
}
