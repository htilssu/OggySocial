package com.oggysocial.oggysocial.services;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DBHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String searchHistory = "CREATE TABLE search_history (id INTEGER PRIMARY KEY AUTOINCREMENT, keyword TEXT)";
        db.execSQL(searchHistory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS search_history");
        onCreate(db);
    }

    public void insertSearchHistory(String keyword) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO search_history (keyword) VALUES ('" + keyword + "')");
    }

    public void deleteSearchHistory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM search_history WHERE id = " + id);
    }

    public void deleteSearchHistory(String keyword) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM search_history WHERE keyword = " + keyword);
    }
}
