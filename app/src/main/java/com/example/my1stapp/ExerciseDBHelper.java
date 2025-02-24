package com.example.my1stapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExerciseDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ExerciseDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_EXERCISE = "exercise";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_TIME = "time";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_EXERCISE + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TYPE + " TEXT NOT NULL," +
                    COLUMN_TIME + " INTEGER NOT NULL" +
                    ")";

    public ExerciseDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 数据库升级逻辑
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE);
        onCreate(db);
    }
}
