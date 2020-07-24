package com.lxqhmlwyh.qingtingfm.utils;

import android.media.MediaPlayer;
import android.util.Log;

import com.lxqhmlwyh.qingtingfm.service.PlayService;

import java.io.IOException;

public class MyPlayer {
    private MediaPlayer fmPlay;
    public static int currentIndex;

    public void playUrl(String url){
        fmPlay=new MediaPlayer();
        try {
            fmPlay.setDataSource(url);
            Log.e("MyPlayer",url);
            //fmPlay.start();
            fmPlay.setOnPreparedListener(onPreparedListener);
            fmPlay.prepare();
            fmPlay.setOnErrorListener(errorListener);
            PlayService.IS_SERVICING=true;
            fmPlay.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.e("MyPlayer","播放结束，下一曲");
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
            Log.e("MyPlayer",e.getMessage());
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

    private MediaPlayer.OnErrorListener errorListener=new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
            Log.e("MyPlayer","播放错误：");
            return false;
        }
    };

    private MediaPlayer.OnPreparedListener onPreparedListener=new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            fmPlay.start();
            Log.e("MyPlayer","加载媒体流完毕，开始播放");
        }
    };

}
