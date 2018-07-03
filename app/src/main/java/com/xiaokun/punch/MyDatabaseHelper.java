package com.xiaokun.punch;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 肖坤 on 2018/7/3.
 *
 * @author 肖坤
 * @date 2018/7/3
 */

public class MyDatabaseHelper extends SQLiteOpenHelper
{
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS Attendance(" +
            "id integer primary key autoincrement," +
            "workTime text," +
            "theoryOffTime text)";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
