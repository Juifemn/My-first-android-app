package com.example.my1stapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeightDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "WeightDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_WEIGHT = "weight";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_WEIGHT = "weight";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_WEIGHT + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_DATE + " TEXT NOT NULL," +
                    COLUMN_WEIGHT + " REAL NOT NULL" +
                    ")";

    public WeightDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 数据库升级逻辑
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHT);
        onCreate(db);
    }
}
