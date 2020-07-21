package com.lxqhmlwyh.qingtingfm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.lxqhmlwyh.qingtingfm.R;

public class PlayActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView fmCoverImg;
    private static String channelName;
    private static String coverUrl;
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
    }
}
