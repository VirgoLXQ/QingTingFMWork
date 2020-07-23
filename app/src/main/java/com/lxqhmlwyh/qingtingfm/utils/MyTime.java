package com.lxqhmlwyh.qingtingfm.utils;


import java.util.Calendar;

public class MyTime {
    public static int dayOFWeek(){
        Calendar calendar=Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
