package com.xiaokun.punch;

import android.os.AsyncTask;

/**
 * @author 肖坤
 * @date 2018/07/16
 */
public class DatabaseInitializer
{

    public static void addPunchBookAsync(final AppDatabase db, final String workTime, final String theoryOffTime)
    {
        DbAsync dbAsync = new DbAsync(db);
        dbAsync.execute(workTime, theoryOffTime);
    }

    public static void addPunchBook(final AppDatabase db, final String workTime, final String theoryOffTime)
    {
        PunchBook punchBook = new PunchBook();
        punchBook.workTime = workTime;
        punchBook.theoryOffTime = theoryOffTime;
//        punchBook.id = System.currentTimeMillis() + "";
        db.punchBookModel().insertPunch(punchBook);
    }

    private static class DbAsync extends AsyncTask<String, Void, Void>
    {

        private final AppDatabase mDb;

        DbAsync(AppDatabase db)
        {
            mDb = db;
        }

        @Override
        protected Void doInBackground(String... strings)
        {
            String workTime = strings[0];
            String offTime = strings[1];
            addPunchBook(mDb, workTime, offTime);
            return null;
        }
    }
}
