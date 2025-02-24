package com.example.my1stapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBOpenHelper extends SQLiteOpenHelper{
    public MyDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE agenda("+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "title TEXT NOT NULL,"+
                "date TEXT NOT NULL,"+
                "time TEXT NOT NULL,"+
                "content TEXT,"+
                "state TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS agenda");
        onCreate(db);
    }
}
