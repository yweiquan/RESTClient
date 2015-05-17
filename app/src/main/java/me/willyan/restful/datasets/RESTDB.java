package me.willyan.restful.datasets;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Will on 2014/10/9.
 */
public class RESTDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "rest.db";

    public static RESTDB restdb;

    public static void init(Context context) {
        if (restdb == null) {
            restdb = new RESTDB(context.getApplicationContext());
        }
    }

    public static RESTDB getDB(){
        return restdb;
    }

    public RESTDB(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        CacheTable.getInstance().createTables(db);
        // TODO 创建其他表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        CacheTable.getInstance().onUpgrade(db, oldVersion, newVersion);
        // TODO 升级其他表
    }

}
