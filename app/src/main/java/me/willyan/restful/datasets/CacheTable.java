package me.willyan.restful.datasets;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Will on 2014/10/17.
 */
public class CacheTable extends SQLTable {

    public static final String NAME = "cache";

    public static final class Columns {
        public static final String KEY = "key";
        public static final String CONTENT = "content";
        public static final String DATE = "date";
    }

    public static final class INDEX {
        public static final int KEY = 1;
        public static final int CONTENT = 2;
        public static final int DATE = 3;
    }

    private static final class Holder {
        public static final CacheTable INSTANCE = new CacheTable();
    }

    public static synchronized CacheTable getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected String getUniqueConstraint() {
        return "UNIQUE (" + Columns.KEY + ") ON CONFLICT REPLACE";
    }

    @Override
    protected Map<String, String> getColumnMapping() {
        final Map<String, String> map = new LinkedHashMap<String, String>();
        map.put(BaseColumns.ID, "INTEGER PRIMARY KEY AUTOINCREMENT");
        map.put(Columns.KEY, "TEXT");           //1
        map.put(Columns.CONTENT, "TEXT");       //2
        map.put(Columns.DATE, "LONG NOT NULL"); //3
        return map;
    }

    @Override
    protected void createTables(SQLiteDatabase db) {
        db.execSQL(toCreateQuery());
    }

    @Override
    protected void dropTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static String getCache(Context context ,String key){
        String result = "";
        Cursor cursor = context.getContentResolver().query(RESTProvider.URI_CACHE, null, Columns.KEY + " LIKE ? ", new String[]{key}, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = cursor.getString(INDEX.CONTENT);
            }
            cursor.close();
        }
        return result;
    }

    public static void insertCache(Context context ,String key, String content){
        ContentValues values = new ContentValues();
        values.put(Columns.KEY, key);
        values.put(Columns.CONTENT, content);
        values.put(Columns.DATE, System.currentTimeMillis());
        context.getContentResolver().insert(RESTProvider.URI_CACHE, values);
    }

    public static void deleteAll(Context context){
        context.getContentResolver().delete(RESTProvider.URI_CACHE, null, null);
    }
}
