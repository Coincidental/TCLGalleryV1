package com.gallery.tclgallery.utils;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private static String[] WEEKS = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};


    /** 获取指定日期为星期几 */
    public static String getWeek(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if(week_index<0){
            week_index = 0;
        }
        return WEEKS[week_index];
    }
    /**
     * Time conversion.
     *
     * @param duration ms.
     * @return such as: {@code 00:00:00}, {@code 00:00}.
     */
    @NonNull
    public static String convertDuration(@IntRange(from = 1, to = Long.MAX_VALUE) long duration) {
        duration /= 1000;
        int hour = (int) (duration / 3600);
        int minute = (int) ((duration - hour * 3600) / 60);
        int second = (int) (duration - hour * 3600 - minute * 60);

        String hourValue = "";
        String minuteValue;
        String secondValue;
        if (hour > 0) {
            if (hour > 10) {
                hourValue = Integer.toString(hour);
            } else {
                hourValue = "0" + hour;
            }
            hourValue += ":";
        }
        if (minute > 0) {
            if (minute > 10) {
                minuteValue = Integer.toString(minute);
            } else {
                minuteValue = "0" + minute;
            }
        } else {
            minuteValue = "00";
        }
        minuteValue += ":";
        if (second > 0) {
            if (second > 10) {
                secondValue = Integer.toString(second);
            } else {
                secondValue = "0" + second;
            }
        } else {
            secondValue = "00";
        }
        return hourValue + minuteValue + secondValue;
    }
}
