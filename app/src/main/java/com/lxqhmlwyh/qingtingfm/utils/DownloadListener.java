package com.lxqhmlwyh.qingtingfm.utils;

public interface DownloadListener {
    void getProgress(int progress);
    void onFailed(String msg);
    void onSuccess(String filePath);
    void OnStart(String newFileName, double fileSize);
    void OnExists(String filePath);
}
