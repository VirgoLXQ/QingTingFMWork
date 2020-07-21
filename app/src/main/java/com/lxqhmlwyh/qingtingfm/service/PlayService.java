package com.lxqhmlwyh.qingtingfm.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;

import androidx.annotation.Nullable;

import java.io.IOException;

public class PlayService extends IntentService {

    private static boolean IS_SERVICING=false;

    public PlayService(String name) {
        super(name);
    }

    public PlayService() {
        super("PlayService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (IS_SERVICING)return;
        MediaPlayer fmPlay=new MediaPlayer();
        try {
            fmPlay.setDataSource("http://lhttp.qingting.fm/live/1756/64k.mp3");
            fmPlay.prepare();
            fmPlay.start();
            IS_SERVICING=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
