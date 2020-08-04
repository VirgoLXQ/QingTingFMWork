package com.lxqhmlwyh.qingtingfm.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.lxqhmlwyh.qingtingfm.entities.PlayingList;
import com.lxqhmlwyh.qingtingfm.utils.MyPlayer;

import java.util.List;

/**
 * 用于在后台播放节目
 */
public class PlayService extends IntentService {

    public static boolean IS_SERVICING=false;
    private static List<PlayingList> PLAYING_LIST;
    private static boolean RUNNING=false;
    private static MyPlayer myPlayer;

    public PlayService(String name) {
        super(name);
    }

    public PlayService() {
        super("PlayService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (IS_SERVICING)return;
        myPlayer=new MyPlayer();
        myPlayer.playUrl(PLAYING_LIST.get(intent.getIntExtra("startIndex",0)).getPlayUrl());

    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
        IS_SERVICING=false;
        if (myPlayer!=null){
            myPlayer.stop();
            myPlayer=null;
        }

        Log.e("PlayService","播放服务开启");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("PlayService","播放服务结束");
    }

    public static void setPlayingList(List<PlayingList> playingList) {
        PLAYING_LIST = playingList;
        IS_SERVICING=false;
    }

    public static List<PlayingList> getPlayingList(){
        return PLAYING_LIST;
    }
}
