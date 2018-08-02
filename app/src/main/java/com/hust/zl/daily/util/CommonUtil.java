package com.hust.zl.daily.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CommonUtil {
    /**
     * 获取当前日期
     *
     * @return 返回格式 yyyyMMdd
     */
    public static String getToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        return sdf.format(new Date());
    }

    /**
     * 获取用于显示的日期
     *
     * @return 返回格式 yyyy年MM月dd日
     */
    public static String getShowDate(String date) {
        if (date.length() != 8) {
            return "";
        }
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String day = date.substring(6, 8);
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.set(Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day));
        String weekDay = new SimpleDateFormat("EEEE", Locale.CHINA).format(calendar.getTime());
        return year + "年" + month + "月" + day + "日 " + weekDay;
    }

}
