package com.xiaokun.punch;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;

/**
 * @author 肖坤
 * @date 2018/07/16
 */
public class HomeViewModel extends AndroidViewModel
{
    private static final String TAG_OUTPUT = "TAG_OUTPUT";
    private final WorkManager mWorkManager;
    private final LiveData<List<WorkStatus>> mStatuses;
    private AppDatabase mDb;
    private LiveData<List<PunchBook>> mPunchBooks;

    public HomeViewModel(@NonNull Application application)
    {
        super(application);

        mWorkManager = WorkManager.getInstance();
        mStatuses = mWorkManager.getStatusesByTag(TAG_OUTPUT);
    }

    public void executeVoice()
    {
        long time = App.mPref.get(VoiceWorker.TIME, 0);

        Toast.makeText(getApplication(), "准备执行", Toast.LENGTH_SHORT).show();
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(VoiceWorker.class)
                .addTag(TAG_OUTPUT)
                .setInputData(new Data.Builder().putLong(VoiceWorker.TIME, time).build())
//                .setInitialDelay(300, TimeUnit.SECONDS)
                .build();
        mWorkManager.beginUniqueWork("task", ExistingWorkPolicy.REPLACE, workRequest);
        mWorkManager.enqueue(workRequest);
    }

    public LiveData<List<WorkStatus>> getOutputStatus()
    {
        return mStatuses;
    }

    /**
     * 创建数据库
     */
    public void createDb()
    {
        mDb = AppDatabase.getInstance();
    }

    /**
     * 添加数据到数据库
     *
     * @param workTime
     * @param theoryOffTime
     */
    public void addPunch(String workTime, String theoryOffTime)
    {
        //这里需要异步
        DatabaseInitializer.addPunchBookAsync(mDb, workTime, theoryOffTime);
    }

    /**
     * 查询数据库
     *
     * @return
     */
    public LiveData<List<PunchBook>> queryPunchBook()
    {
        //第一种实现方法,利用MediatorLiveData来实现
        LiveData<List<PunchBook>> allPunches = mDb.punchBookModel().findAllPunches();
        ((MediatorLiveData<List<PunchBook>>) mPunchBooks).addSource(allPunches, new Observer<List<PunchBook>>()
        {
            @Override
            public void onChanged(@Nullable List<PunchBook> punchBooks)
            {
                ((MediatorLiveData<List<PunchBook>>) mPunchBooks).setValue(punchBooks);
            }
        });


        //第二种实现方法,利用MutableLiveData来实现,直接从数据库中拿到List对象,然后直接postValue,会直接触发onChange方法
        // List<PunchBook> allPunches = mDb.punchBookModel().findAllPunches();
        // ((MutableLiveData) mPunchBooks).postValue(allPunches);
        return mPunchBooks;
    }

    public LiveData<List<PunchBook>> getPunchBooks()
    {
        if (mPunchBooks == null)
        {
            mPunchBooks = new MediatorLiveData<>();
        }

        //第二种方法实现
//        if (mPunchBooks == null)
//        {
//            mPunchBooks = new MutableLiveData<>();
//        }

        return mPunchBooks;
    }
}
