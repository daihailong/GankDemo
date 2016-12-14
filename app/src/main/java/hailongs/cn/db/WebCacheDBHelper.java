package hailongs.cn.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import hailongs.cn.utils.Constants;

/**
 * Created by dhl on 2016/12/10.
 */

public class WebCacheDBHelper extends SQLiteOpenHelper {

    private final static String SQL = "create table if not exists Cache (" +
            "id INTEGER primary key autoincrement," +
            "newsId INTEGER unique," +
            "json TEXT)";

    public WebCacheDBHelper(Context mContext, int version) {
        super(mContext, Constants.WEB_DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
