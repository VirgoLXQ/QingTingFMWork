package com.lxqhmlwyh.qingtingfm.utils;


public interface MusicControllerInterface {
    void onProgressChange(int progress);
    void onPlayFinish();
    void onDownloadProgressChange(int progress);
    void onDownloadFinish();

}
