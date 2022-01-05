package com.zhy.lib_common.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by ZY on 2019/2/26
 * DESC: class DateUtils
 */
public class DateUtils {

    /**
     * 获取当前年月日 yyyy-MM-dd格式
     *
     * @param time 传入时间值
     * @return
     */
    public static String getDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return format.format(time);
    }


    /**
     * 获取当前年月日 yyyy-MM-dd格式
     *
     * @param time 传入时间值
     * @return
     */
    public static String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("mm:ss", Locale.CHINA);
        return format.format(time);
    }

    /**
     * 获取媒体文件播放时间，格式化输出
     *
     * @param ms 毫秒
     * @return 格式化后的结果：hh:mm:ss
     */
    public static String getMediaTime(int ms) {
        int hour, mintue, second;

        //计算小时 1 h = 3600000 ms
        hour = ms / 3600000;

        //计算分钟 1 min = 60000 ms
        mintue = (ms - hour * 3600000) / 60000;

        //计算秒钟 1 s = 1000 ms
        second = (ms - hour * 3600000 - mintue * 60000) / 1000;

        //格式化输出，补零操作
        String sHour, sMintue, sSecond;
        if (hour < 10) {
            sHour = "0" + hour;
        } else {
            sHour = String.valueOf(hour);
        }

        if (mintue < 10) {
            sMintue = "0" + mintue;
        } else {
            sMintue = String.valueOf(mintue);
        }

        if (second < 10) {
            sSecond = "0" + second;
        } else {
            sSecond = String.valueOf(second);
        }

        return sHour + ":" + sMintue + ":" + sSecond;
    }
}
