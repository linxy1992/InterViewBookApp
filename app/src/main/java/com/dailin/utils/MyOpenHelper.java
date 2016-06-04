package com.dailin.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/4/18.
 */
public class MyOpenHelper extends SQLiteOpenHelper {

    private static final String DBNAME = "question.db";
    private static final int DBVERSION = 1;

    public MyOpenHelper(Context context){
        super(context,DBNAME,null,DBVERSION);
    }

    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table ques_ans(_id integer primary key autoincrement," +
                "question text,answer text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
