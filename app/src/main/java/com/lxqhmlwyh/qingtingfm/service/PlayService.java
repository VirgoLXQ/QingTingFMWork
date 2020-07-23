package com.lxqhmlwyh.qingtingfm.service;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;

import androidx.annotation.Nullable;

import com.lxqhmlwyh.qingtingfm.pojo.PlayingList;
import com.lxqhmlwyh.qingtingfm.utils.MyPlayer;

import java.io.IOException;
import java.util.List;

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
        myPlayer.playUrl(PLAYING_LIST.get(0).getPlayUrl());
    }

    public static void setPlayingList(List<PlayingList> playingList) {
        PLAYING_LIST = playingList;
        IS_SERVICING=false;
    }

    public static List<PlayingList> getPlayingList(){
        return PLAYING_LIST;
    }
}
