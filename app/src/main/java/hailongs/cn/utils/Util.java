package hailongs.cn.utils;

import android.support.v7.widget.RecyclerView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dhl on 2016/11/28.
 */

public class Util {
    /**
     * 简单格式化日期
     *
     * @param date
     * @return
     */
    public static String formatDate(String date) {
        //Logger.i("date length = " + date);
        //Logger.i("date = null ? " + (date == null));
        String formatDate = new String(date.substring(0, 10));
        return formatDate;
    }

    public static String date(String date) {
        //Logger.i("date length = " + date);
        //Logger.i("date = null ? " + (date == null));
        String formatDate = new String(date.substring(2, 10));
        return formatDate;
    }


    /**
     * 将 String 数组转换成 ArrayList
     *
     * @param images
     * @return
     */
    public static List<String> toList(String[] images) {
        List<String> urlList = new ArrayList<>();
        for (String url : images) {
            urlList.add(url);
        }
        return urlList;
    }


    /**
     * 判断recyclerview是否滑动到底部了
     *
     * @param recyclerView
     * @return
     */
    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }
}
