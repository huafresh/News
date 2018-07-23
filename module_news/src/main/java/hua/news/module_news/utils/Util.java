package hua.news.module_news.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author hua
 * @version 2018/4/9 15:27
 */

public class Util {
    private static final long ONE_SECOND = 1000;
    private static final long ONE_MINUTE = ONE_SECOND * 60;
    private static final long ONE_HOUR = ONE_MINUTE * 60;
    private static final long ONE_DAY = ONE_HOUR * 24;

    public static String formatTime(String updateTime) {
        String result = "";
        try {
            SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
            simpleDateFormat.applyPattern("yyyy/MM/dd HH:mm:ss");
            Date date = simpleDateFormat.parse(updateTime);
            long pass = date.getTime() - System.currentTimeMillis();
            if (pass < ONE_DAY * 30) {
                if (pass < ONE_MINUTE) {
                    result = "刚刚";
                } else if (pass < ONE_HOUR) {
                    result = String.format(Locale.CHINA, "%d分钟前", pass / ONE_MINUTE);
                } else if (pass < ONE_DAY) {
                    result = String.format(Locale.CHINA, "%d小时前", pass / ONE_HOUR);
                } else {
                    result = String.format(Locale.CHINA, "%d天前", pass / ONE_DAY);
                }
            } else {
                simpleDateFormat.applyPattern("MM月DD日 HH:mm:ss");
                result = simpleDateFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;

    }

}
