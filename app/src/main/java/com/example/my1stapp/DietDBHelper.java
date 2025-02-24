package com.example.my1stapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DietDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DietDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_DIET = "diet";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_CALORIES = "calories";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_DIET + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_DATE + " TEXT NOT NULL," +
                    COLUMN_CALORIES + " INTEGER NOT NULL" +
                    ")";

    public DietDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIET);
        onCreate(db);
    }
}
