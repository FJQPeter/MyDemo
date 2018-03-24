package com.instant.datastorage.util;

import com.instant.datastorage.common.Config;
import org.apache.commons.lang.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 数据格式转换工具类
 * Created by nijie on 2016/8/4.
 */
public class DateValueFormat {

    private static final SimpleDateFormat DATE_FORMAT_SECOND =  new SimpleDateFormat(Config.getString("dataformat.date.pattern.second"));
    private static final SimpleDateFormat DATE_FORMAT_DAY =  new SimpleDateFormat(Config.getString("dataformat.date.pattern.day"));

    public static Long parseSecondStringToDate(String str){
        if(StringUtils.isEmpty(str))
            return null;
        try {
            Date date = DATE_FORMAT_SECOND.parse(str);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Long parseDayStringToDate(String str){
        if(StringUtils.isEmpty(str))
            return null;
        try {
            Date date = DATE_FORMAT_DAY.parse(str);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatTimeToString(Object time){
        if(time == null){
            return null;
        }
        long timeValue;
        if(time instanceof Integer){
            timeValue = ((Integer) time).longValue();
        }else{
            timeValue = ((Long)time).longValue();
        }
        if(timeValue == 0L)
            return null;
        return DATE_FORMAT_SECOND.format(new Date(timeValue));
    }

    public static int compareTime(String date1,String date2) throws ParseException {
        long time1 = DATE_FORMAT_SECOND.parse(date1).getTime();
        long time2 = DATE_FORMAT_SECOND.parse(date2).getTime();
        if(time1 > time2)
            return 1;
        if(time1 == time2)
            return 0;
        return -1;
    }
    
    public static Timestamp formatDate(Object time) {
		Long timeL = (Long)time;
		return new Timestamp(timeL);
	}

    public static void main(String[] args){

    }
}
