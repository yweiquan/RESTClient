package me.willyan.restful.datasets;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.Map;

/**
 * Created by Will on 2014/10/9.
 *
 * ContentProvider查询的基类, 每个数据库的表都需要继承SQLTable，创建独立的REST类
 *
 */
public abstract class SQLTable {

    public abstract String getName();
    protected abstract String getUniqueConstraint();
    protected abstract Map<String, String> getColumnMapping();
    protected abstract void createTables(SQLiteDatabase db);
    protected abstract void dropTables(SQLiteDatabase db);

    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    public static class BaseColumns {
        public final static String ID = "_id"; // 必须是_id
    }

    public String toCreateQuery() {
        String createQuery = "CREATE TABLE IF NOT EXISTS " + getName() + " (";

        Map<String, String> columns = getColumnMapping();

        for (String column : columns.keySet()) {
            createQuery += column + " " + columns.get(column) + ", ";
        }

        createQuery += getUniqueConstraint() + ");";

        return createQuery;
    }

    public String toCreateTrigger(String sql) {
        return sql;
    }

    public Cursor query(final SQLiteDatabase database, final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        return database.query(getName(), projection, selection, selectionArgs, null, null, sortOrder);
    }

    public long insert(final SQLiteDatabase database, final Uri uri, final ContentValues values) {
        return database.insert(getName(), null, values);
    }

    public long insert(final SQLiteDatabase database, final ContentValues values) {
        return insert(database, null, values);
    }

    public int update(final SQLiteDatabase database, final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
        return database.update(getName(), values, selection, selectionArgs);
    }

    public int update(final SQLiteDatabase database, final ContentValues values, final String selection, final String[] selectionArgs) {
        return update(database, null, values, selection, selectionArgs);
    }

    public int delete(final SQLiteDatabase database, final Uri uri, final String selection, final String[] selectionArgs) {
        return database.delete(getName(), selection, selectionArgs);
    }

    public int delete(final SQLiteDatabase database, final String selection, final String[] selectionArgs) {
        return delete(database, null, selection, selectionArgs);
    }

}
