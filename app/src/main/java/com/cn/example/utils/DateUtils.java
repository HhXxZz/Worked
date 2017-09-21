package com.cn.example.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/21.
 */

public class DateUtils {

    public static String getUpdateDate(String ymd){
        String year = ymd.substring(0,4);
        String month = ymd.substring(4,6);
        String day = ymd.substring(6,8);

        return year+"/"+month+"/"+day;
    }


    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate(long date) {
        Date currentTime = new Date(date);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


}
