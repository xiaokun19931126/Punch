package com.xiaokun.punch;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

/**
 * @author 肖坤
 * @date 2018/07/16
 */

@Database(entities = {PunchBook.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    public static final String DB_NAME = "APPData.db";

    private static volatile AppDatabase instance;

    public abstract PunchBookDao punchBookModel();

    public static AppDatabase getInstance()
    {
        if (instance == null)
        {
            synchronized (AppDatabase.class)
            {
                if (instance == null)
                {
                    instance = Room.databaseBuilder(App.getApplication(), AppDatabase.class, DB_NAME)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return instance;
    }

}
