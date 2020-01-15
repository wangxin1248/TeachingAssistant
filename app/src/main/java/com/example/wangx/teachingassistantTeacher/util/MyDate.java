package com.example.wangx.teachingassistantTeacher.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *数据显示
 */
public class MyDate {
    public static String getCurrentDate(String formatDate){
        SimpleDateFormat format = new SimpleDateFormat(formatDate);
        Date date = new Date(System.currentTimeMillis());
        String currentDate = format.format(date);
        return currentDate;
    }
}
