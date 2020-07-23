package com.lxqhmlwyh.qingtingfm.utils;

import android.media.MediaPlayer;
import android.util.Log;

import com.lxqhmlwyh.qingtingfm.service.PlayService;

import java.io.IOException;

public class MyPlayer {
    private MediaPlayer fmPlay;
    private int currentIndex=0;

    public void playUrl(String url){
        fmPlay=new MediaPlayer();
        try {
            fmPlay.setDataSource(url);
            Log.e("MyPlayer",url);
            fmPlay.prepare();
            fmPlay.start();
            PlayService.IS_SERVICING=true;
            fmPlay.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (hasNext()){
                        currentIndex++;
                        playUrl(PlayService.getPlayingList().get(currentIndex).getPlayUrl());
                    }else{
                        stop();
                        return;
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean next(){
        if (hasNext()){
            currentIndex++;
            playUrl(PlayService.getPlayingList().get(currentIndex).getPlayUrl());
            return true;
        }else{
            return false;
        }
    }

    public boolean previous(){
        if (currentIndex-1>=0){
            currentIndex--;
            playUrl(PlayService.getPlayingList().get(currentIndex).getPlayUrl());
            return true;
        }else{
            return false;
        }
    }

    public void stop(){
        PlayService.IS_SERVICING=false;
        fmPlay.release();
    }

    public boolean hasNext(){
        if (currentIndex+1>=PlayService.getPlayingList().size()){
            return false;
        }else{
            return true;
        }
    }
}
