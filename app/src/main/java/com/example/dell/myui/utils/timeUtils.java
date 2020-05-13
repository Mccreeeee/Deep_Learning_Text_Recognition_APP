package com.example.dell.myui.utils;

import java.util.Calendar;

public class timeUtils {
    //获取现在的时间
    public static String getCurrentTime(){

//获取系统的日期
//年
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
//月
        int month = calendar.get(Calendar.MONTH)+1;
//日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
//获取系统时间
//小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//分钟
        int minute = calendar.get(Calendar.MINUTE);
//秒
        int second = calendar.get(Calendar.SECOND);

        String date = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
        String time = String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + String.valueOf(second);
        return date + "-" + time;
    }
}
