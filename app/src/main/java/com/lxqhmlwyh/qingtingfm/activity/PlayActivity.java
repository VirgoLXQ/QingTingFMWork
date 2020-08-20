package com.lxqhmlwyh.qingtingfm.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.lxqhmlwyh.qingtingfm.R;
import com.lxqhmlwyh.qingtingfm.entities.PlayingList;
import com.lxqhmlwyh.qingtingfm.utils.MusicControllerInterface;
import com.lxqhmlwyh.qingtingfm.utils.MyPlayer;

public class PlayActivity extends AppCompatActivity implements MusicControllerInterface, SeekBar.OnSeekBarChangeListener
,View.OnClickListener{

    private Toolbar toolbar;
    private ImageView fmCoverImg;
    private TextView titleView;
    private TextView broadcasterView;
    private TextView startTimeView;
    private TextView endTimeView;
    private SeekBar seekBar;
    private TextView cachingView;
    private ImageView playView,nextView,previousView;

    /**
     * 用于记录数据
     */
    private static String channelName;
    private static String coverUrl;
    private static String title;
    private static String broadcaster;
    private static String START_TIME;
    private static String END_TIME;
    //private static int DURATION;
    private MyPlayer myPlayer;
    //private String firstUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        initData();
        initView();
        //Toast.makeText(this, "onCreate in PlayActivity", Toast.LENGTH_SHORT).show();
    }
    private void initView(){
        cachingView=findViewById(R.id.play_text_caching);
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
        //seekBar.setMax(DURATION);
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
        playView=findViewById(R.id.play_or_pause);
        nextView=findViewById(R.id.play_next);
        previousView=findViewById(R.id.play_previous);
        seekBar=findViewById(R.id.play_seek_bar);

        coverUrl=getIntent().getStringExtra("cover")==null?coverUrl:getIntent().getStringExtra("cover");
        channelName=getIntent().getStringExtra("channelName")==null?channelName:getIntent().getStringExtra("channelName");
        broadcaster=getIntent().getStringExtra("broadcaster")==null?broadcaster:getIntent().getStringExtra("broadcaster");
        title=getIntent().getStringExtra("title")==null?title:getIntent().getStringExtra("title");
        START_TIME=getIntent().getStringExtra("start_time")==null?START_TIME:getIntent().getStringExtra("start_time");
        END_TIME=getIntent().getStringExtra("end_time")==null?END_TIME:getIntent().getStringExtra("end_time");
        /*DURATION=getIntent().getIntExtra("duration",0)==0?DURATION:
                getIntent().getIntExtra("duration",0)/60;*/

        //判断时候有东西可以播放
        myPlayer=MyPlayer.getMyPlayer();
        if(getIntent().getBooleanExtra("need",false)){
            myPlayer.setController(this);
            myPlayer.start();
            playView.setImageResource(R.mipmap.pause);
            playView.setOnClickListener(this);
            nextView.setOnClickListener(this);
            previousView.setOnClickListener(this);
            seekBar.setOnSeekBarChangeListener(this);
        }else{
            if (myPlayer!=null){
                MediaPlayer mediaPlayer=myPlayer.getMediaPlayer();
                if (mediaPlayer!=null){
                    playView.setImageResource(R.mipmap.pause);
                    playView.setOnClickListener(this);
                    nextView.setOnClickListener(this);
                    previousView.setOnClickListener(this);
                    seekBar.setOnSeekBarChangeListener(this);
                    if (mediaPlayer.isPlaying()){
                        playView.setImageResource(R.mipmap.pause);
                    }else{
                        playView.setImageResource(R.mipmap.play);
                    }
                    return;
                }
            }
            Toast.makeText(this, "没有内容可以播放", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onProgressChange(int progress) {
        //Log.e("PlayActivity","正在播放"+progress);
        seekBar.post(()->{
            seekBar.setProgress(progress);
        });
    }

    @Override
    public void onPlayFinish() {
        //Toast.makeText(this, "播放结束", Toast.LENGTH_SHORT).show();
        //playView.setImageResource(R.mipmap.play);
        myPlayer.next();
        updateUI();
    }

    @Override
    public void onDownloadProgressChange(int progress) {
        seekBar.post(()->{
            seekBar.setSecondaryProgress(progress);
        });
        cachingView.post(()->{
            cachingView.setVisibility(View.VISIBLE);
            cachingView.setText("正在缓冲，请稍后..."+progress+"%");
        });
    }

    @Override
    public void onDownloadFinish() {
        Log.e("PlayActivity","下载完成");
        cachingView.post(()->cachingView.setVisibility(View.INVISIBLE));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //if (myPlayer==null)return;
        int duration=myPlayer.getDuration();
        double currentSeek=seekBar.getProgress()/100f;
        int seekTo= (int) (duration*currentSeek);
        myPlayer.seekTo(seekTo);
    }

    /**
     * 按钮点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (myPlayer.getMediaPlayer()==null)return;
        MediaPlayer mediaPlayer=myPlayer.getMediaPlayer();
        switch (v.getId()){
            case R.id.play_or_pause:
                //判断是否正在播放
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    playView.setImageResource(R.mipmap.play);
                }else{
                    mediaPlayer.start();
                    playView.setImageResource(R.mipmap.pause);
                }
                break;
            case R.id.play_next:
                myPlayer.next();
                updateUI();
                break;
            case R.id.play_previous:
                myPlayer.previous();
                updateUI();
                break;
        }
    }

    /**
     * 切换节目时，更新标题和时间
     */
    private void updateUI(){
        PlayingList info=myPlayer.getListInfo();
        START_TIME=info.getStartTime();
        END_TIME=info.getEndTime();
        broadcaster=info.getBroadcasters();
        title=info.getProgramName();

        startTimeView.setText(START_TIME);
        endTimeView.setText(END_TIME);
        broadcasterView.setText(broadcaster);
        titleView.setText(title);
    }
}
