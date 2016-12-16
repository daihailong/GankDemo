package hailongs.cn.utils;

import android.support.v7.widget.RecyclerView;

import com.orhanobut.logger.Logger;

import java.io.File;
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

//    public void clearWebViewCache() {
//
//        //清理Webview缓存数据库
//        try {
//            //deleteDatabase("webview.db");
//            //deleteDatabase("webviewCache.db");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //WebView 缓存文件
//        File appCacheDir = new File(getFilesDir().getAbsolutePath() + Constants.APP_CACHE_DIRNAME);
//        Logger.i(TAG + "appCacheDir path=" + appCacheDir.getAbsolutePath());
//
//        File webviewCacheDir = new File(getCacheDir().getAbsolutePath() + "/webviewCache");
//        Logger.i(TAG + "webviewCacheDir path=" + webviewCacheDir.getAbsolutePath());
//
//        //删除webview 缓存目录
//        if (webviewCacheDir.exists()) {
//            Logger.i("webview缓存目录存在过！");
//            deleteFile(webviewCacheDir);
//        }
//        //删除webview 缓存 缓存目录
//        if (appCacheDir.exists()) {
//            Logger.i("app缓存目录存在过！");
//            deleteFile(appCacheDir);
//        }
//    }
//
//    /**
//     * 递归删除 文件/文件夹
//     *
//     * @param file
//     */
//    public void deleteFile(File file) {
//
//        Logger.i(TAG, "delete file path=" + file.getAbsolutePath());
//
//        if (file.exists()) {
//            if (file.isFile()) {
//                file.delete();
//            } else if (file.isDirectory()) {
//                File files[] = file.listFiles();
//                for (int i = 0; i < files.length; i++) {
//                    deleteFile(files[i]);
//                }
//            }
//            file.delete();
//        } else {
//            Logger.e(TAG, "delete file no exists " + file.getAbsolutePath());
//        }
//    }

}
