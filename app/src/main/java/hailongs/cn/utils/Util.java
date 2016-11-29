package hailongs.cn.utils;

import com.orhanobut.logger.Logger;

/**
 * Created by dhl on 2016/11/28.
 */

public class Util {
    public static String formatDate(String date) {
        Logger.i("date length = " + date);
        Logger.i("date = null ? " + (date == null));
        String formatDate = new String(date.substring(0,10));
        return formatDate;
    }
}
