package hailongs.cn.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import hailongs.cn.utils.Constants;

/**
 * Created by dhl on 2016/12/8.
 */

public class CacheDBHelper extends SQLiteOpenHelper {

    private final static String SQL = "create table if not exists cache(" +
            "id INTEGER primary key autoincrement," +
            "type int unique," +
            "json TEXT" +
            ")";

    public CacheDBHelper(Context mContext, int version) {
        super(mContext, Constants.DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
