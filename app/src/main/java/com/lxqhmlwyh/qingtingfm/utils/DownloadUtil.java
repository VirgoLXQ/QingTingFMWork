package com.lxqhmlwyh.qingtingfm.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 李兴权
 * 用于边缓存边播放，达到快速播放的目的
 * 2020年8月16日
 */
public class DownloadUtil {

    private String downloadDirRoot;

    public DownloadUtil(Context context){
        this.downloadDirRoot=context.getFilesDir().getPath()+"/download/";
    }

    public void download(final String downloadUrl, final DownloadListener listener){

        /*OkHttpClient okHttpClient=new OkHttpClient();
        Request downLoadRequest=new Request.Builder()
                .url(downloadUrl)
                .build();*/

        //判断文件是否以及存在
        String saveName=downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
        File newFile=new File(downloadDirRoot,saveName);
        if (newFile.exists()){
            listener.OnExists(downloadDirRoot+saveName);
            return;
        }

        CommonHttpRequest.getHttp(downloadUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailed(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                long contentLength=response.body().contentLength();
                String saveName=downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
                listener.OnStart(saveName,contentLength);
                File dir=new File(downloadDirRoot);
                if (!dir.exists()){
                    dir.mkdirs();
                }
                File newFile=new File(downloadDirRoot,saveName);
                /*if (newFile.exists()){
                    listener.OnExists(downloadUrl+saveName);
                    response.close();
                    return;
                }*/
                Log.e("createNewFile",newFile.createNewFile()+"");
                InputStream inputStream;
                byte[] buf=new byte[2048];
                int len;
                FileOutputStream fileOutputStream;

                inputStream=response.body().byteStream();
                fileOutputStream=new FileOutputStream(newFile);
                long downloadedContent=0;
                while ((len=inputStream.read(buf))!=-1){
                    fileOutputStream.write(buf,0,len);
                    downloadedContent+=len;
                    int progress = (int) (downloadedContent * 1.0f / contentLength * 100);
                    listener.getProgress(progress);
                }
                listener.onSuccess(downloadDirRoot+saveName);
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
                response.close();
            }
        });
        /*okHttpClient.newCall(downLoadRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailed(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                long contentLength=response.body().contentLength();
                String saveName=downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
                listener.OnStart(saveName,contentLength);
                File dir=new File(downloadDirRoot);
                if (!dir.exists()){
                    dir.mkdirs();
                }
                File newFile=new File(downloadDirRoot,saveName);
                if (newFile.exists()){
                    listener.OnExists();
                    response.close();
                    return;
                }
                Log.e("createNewFile",newFile.createNewFile()+"");
                InputStream inputStream;
                byte[] buf=new byte[2048];
                int len;
                FileOutputStream fileOutputStream;

                inputStream=response.body().byteStream();
                fileOutputStream=new FileOutputStream(newFile);
                long downloadedContent=0;
                while ((len=inputStream.read(buf))!=-1){
                    fileOutputStream.write(buf,0,len);
                    downloadedContent+=len;
                    int progress = (int) (downloadedContent * 1.0f / contentLength * 100);
                    listener.getProgress(progress);
                }
                listener.onSuccess(downloadDirRoot+saveName);
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
                response.close();
            }
        });*/
    }

    /*public String getDownloadDirRoot() {
        return downloadDirRoot;
    }*/

    /**
     * 清除缓存
     * @param context
     */
    public static void clearCache(Context context){
        String cacheDir=context.getFilesDir().getPath()+"/download/";
        File file=new File(cacheDir);
        String[] files= file.list();
        if (files.length==0)return;
        for(String name:files){
            File temp=new File(cacheDir,name);
            boolean delete= temp.delete();
            if (delete){
                Log.e("删除文件","成功删除"+cacheDir+name);
            }else{
                Log.e("删除文件","删除"+cacheDir+name+"失败");
            }
        }
    }
}
