package com.example.dell.unittv.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dell-pc on 2016/3/14.
 */
public class TimeManager {
    public static String getStrTime1(String cc_time){
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time*1000L));
        return re_StrTime;
    }

    public static String getStrTime2(String cc_time){
        String re_StrTime = null;
        System.out.println("time=============" + cc_time);
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH时mm分");
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time*1000L));
        return re_StrTime;
    }

    public static String getStrTime3(String cc_time){
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd  HH:mm:ss");
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time*1000L));
        return re_StrTime;
    }

    public static String getStrTime4(String cc_time){
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time*1000L));
        return re_StrTime;
    }

    public static String getStrTime5(String cc_time){
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time*1000L));
        return re_StrTime;
    }


}
