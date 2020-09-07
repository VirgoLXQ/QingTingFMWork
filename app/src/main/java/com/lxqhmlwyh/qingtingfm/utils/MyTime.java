package com.lxqhmlwyh.qingtingfm.utils;


import java.util.Calendar;

/**
 * 时间工具
 */
public class MyTime {
    private static Calendar calendar=Calendar.getInstance();

    /**
     * 获取今天是星期几
     * @return
     */
    public static int dayOFWeek(){
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取今天是多少号
     * @return
     */
    public static String getDate(){
        int year=calendar.get(Calendar.YEAR);
        int monthInt=1+calendar.get(Calendar.MONTH);
        //int monthInt=9;
        String month=monthInt>9?monthInt+"":"0"+monthInt;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        String dayStr=day>9?day+"":"0"+day;
        return ""+year+month+dayStr;
    }

    /**
     * 用于拼接节目的播放地址
     * @param channelId
     * @param startTime
     * @param endTime
     * @return
     */
    public static String changeToPlayUrl(int channelId,String startTime,String endTime){
        startTime=startTime.replace(":","");
        endTime=endTime.replace(":","");
        String urlStr="https://lcache.qingting.fm/cache/"+getDate()+"/"
                +channelId+"/"+channelId+"_"+getDate()+"_"+startTime+
                "_"+endTime+"_24_0.aac";
        return urlStr;
    }
}
