package me.willyan.restful.datasets;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.SparseArray;

import me.willyan.restful.BuildConfig;

/**
 * Created by Will on 2014/10/9.
 */
public class RESTProvider extends ContentProvider {

    public static final String AUTHORITY = BuildConfig.PROVIDER_AUTHORITY ;
    public static final Uri URI_CACHE = Uri.parse("content://" + AUTHORITY + "/" + Paths.CACHE);
    // TODO 其他表名对应的Uri

    private static final class Paths {
        private static final String CACHE = CacheTable.NAME;
        // TODO 其他表名
    }

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static SparseArray<SQLTable> sUriMatchToSQLTableMap = new SparseArray<SQLTable>();
    private int URI_MATCH = 0;

    @Override
    public boolean onCreate() {
        registerTable(Paths.CACHE, CacheTable.getInstance());
        // TODO 注册其他表
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLTable table = getSQLTable(uri);
        if (table != null) {
            Cursor cursor = table.query(getDB(), uri, projection, selection, selectionArgs, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLTable table = getSQLTable(uri);
        Uri result_uri = null;
        if (table != null) {
            long _id = table.insert(getDB(), uri, values);
            result_uri = Uri.parse(uri.toString() + "/" + _id);
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return result_uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLTable table = getSQLTable(uri);
        if (table != null) {
            int count = table.delete(getDB(), uri, selection, selectionArgs);
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }

        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLTable table = getSQLTable(uri);
        if (table != null) {
            int count = table.update(getDB(), uri, values, selection, selectionArgs);
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }

        return 0;
    }

    private void registerTable(String path, SQLTable table) {
        final int match = URI_MATCH++;
        sUriMatcher.addURI(AUTHORITY, path, match);
        sUriMatchToSQLTableMap.put(match, table);
    }

    private static synchronized SQLTable getSQLTable(Uri uri) {
        int uriMatch = sUriMatcher.match(uri);

        if (sUriMatchToSQLTableMap.indexOfKey(uriMatch) >= 0)
            return sUriMatchToSQLTableMap.get(uriMatch);

        return null;
    }

    private synchronized SQLiteDatabase getDB() {
        return RESTDB.getDB().getWritableDatabase();
    }
}
