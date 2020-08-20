package com.lxqhmlwyh.qingtingfm.utils;

import android.media.MediaPlayer;
import android.util.Log;

import com.lxqhmlwyh.qingtingfm.entities.PlayingList;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 播放器对象
 */

public class MyPlayer implements DownloadListener,MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener{

    private MusicControllerInterface controllerInterface;
    private static MyPlayer MY_PLAYER;
    private List<PlayingList> musicList;
    private MediaPlayer mediaPlayer;
    private DownloadUtil downloadUtil;
    private ScheduledExecutorService scheduledThreadPool;
    private int duration;
    private int listIndex;
    private int listCount;

    /*private MyPlayer(MusicControllerInterface controller){
        downloadUtil=new DownloadUtil(MyApplication.context);
        this.controllerInterface=controller;
    }*/

    private MyPlayer(){
        downloadUtil=new DownloadUtil(MyApplication.context);
        scheduledThreadPool= Executors.newScheduledThreadPool(1);
    }
    /*public static MyPlayer getInstance(MusicControllerInterface controller){
        if (MY_PLAYER==null){
            MY_PLAYER=new MyPlayer(controller);
            return MY_PLAYER;
        }
        return MY_PLAYER;
    }*/

    /**
     * 无参实例化
     * @return
     */
    public static MyPlayer getInstance(){
        if (MY_PLAYER==null){
            MY_PLAYER=new MyPlayer();
            return MY_PLAYER;
        }
        return MY_PLAYER;
    }

    public void setController(MusicControllerInterface controller){
        this.controllerInterface=controller;
    }

    /**
     * 获取播放器对象
     * @return
     */
    public static MyPlayer getMyPlayer(){
        if (MY_PLAYER==null){
            Log.e("getMyPlayer","对象为空");
            return null;
        }
        return MY_PLAYER;
    }

    /**
     * 获得播放器对象
     * @return
     */
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    /**
     * 设置播放列表
     * @param musicList
     */
    public void setMusicList(List<PlayingList> musicList) {
        this.musicList = musicList;
        listCount=musicList.size();
    }

    /**
     * 重置播放器
     */
    public void resetPlayer(){
        Log.e("resetPlayer","重置播放器");
        if (mediaPlayer==null)return;
        mediaPlayer.stop();
        scheduledThreadPool.shutdownNow();
        scheduledThreadPool=null;
        mediaPlayer.release();
        mediaPlayer=null;
    }

    public void prepare(String url){
        downloadUtil.download(url,this);
    }

    /**
     * 开始播放
     * @param filePath 文件路径
     */
    public void startPlayer(String filePath){
        mediaPlayer=new MediaPlayer();
        scheduledThreadPool= Executors.newScheduledThreadPool(1);
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.start();
            duration=mediaPlayer.getDuration();
            Log.e("startPlayer","开始播放");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Runnable runnable= () -> {
            int current=mediaPlayer.getCurrentPosition();
            double percentD=(current*1.0)/(duration*1.0);
            int percent= (int) (percentD*100);
            controllerInterface.onProgressChange(percent);
        };
        scheduledThreadPool.scheduleAtFixedRate(runnable,0,1, TimeUnit.SECONDS);
    }

    /**
     * 播放到指定位置
     * @param position
     */
    public void seekTo(int position){
        mediaPlayer.seekTo(position);
    }

    /**
     * 开始播放，在activity中调用
     */
    public void start(){
        String url=musicList.get(listIndex).getPlayUrl();
        Log.e("播放",url);
        prepare(url);
    }

    /**
     * 下一曲
     */
    public void next(){
        resetPlayer();
        if (listIndex+1<listCount){
            listIndex++;
        }else{
            listIndex=0;
        }
        start();
        //getListInfo();
    }

    /**
     * 上一曲
     */
    public void previous(){
        resetPlayer();
        if (listIndex-1==-1){
            listIndex=listCount-1;
        }else{
            listIndex--;
        }
        start();
        //getListInfo();
    }

    public PlayingList getListInfo(){
        return musicList.get(listIndex);
    }

    /**
     * 获取音频总时长
     * @return
     */
    public int getDuration() {
        return duration;
    }

    /**
     *
     * @param listIndex
     */
    public void setListIndex(int listIndex) {
        this.listIndex = listIndex;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.e("onCompletion","播放完毕，下一曲");
        controllerInterface.onPlayFinish();
        //next();
    }

    @Override
    public void getProgress(int progress) {
        controllerInterface.onDownloadProgressChange(progress);
    }

    @Override
    public void onFailed(String msg) {
        Log.e("onFailed","下载失败："+msg);
    }

    @Override

    public void onSuccess(String filePath) {
        Log.e("onSuccess","下载成功："+filePath);
        startPlayer(filePath);
        controllerInterface.onDownloadFinish();
    }

    @Override
    public void OnStart(String newFileName, double fileSize) {
        Log.e("OnStart","开始下载文件："+newFileName);
    }

    @Override
    public void OnExists(String filePath) {
        Log.e("OnExists","文件已存在，直接播放"+filePath);
        //resetPlayer();
        startPlayer(filePath);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e("onError","播放错误：what="+what+"；extra"+extra);
        return false;
    }
}
