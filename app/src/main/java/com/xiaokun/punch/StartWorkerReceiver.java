package com.xiaokun.punch;

import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/07/20
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class StartWorkerReceiver extends BroadcastReceiver
{
    private static final String TAG_OUTPUT = "TAG_OUTPUT";
    private static final String TAG = "StartWorkerReceiver";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        long time = App.mPref.get(VoiceWorker.TIME, 0);
        Toast.makeText(context, "开机" + time, Toast.LENGTH_SHORT).show();
        Log.e(TAG, "(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + time);
//        WorkManager manager = WorkManager.getInstance();
//        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(VoiceWorker.class)
//                .addTag(TAG_OUTPUT)
//                .setInputData(new Data.Builder().putLong(VoiceWorker.TIME, time).build())
//                .build();
//        manager.beginUniqueWork("task", ExistingWorkPolicy.REPLACE, workRequest);
//        manager.enqueue(workRequest);
        Intent intent1 = new Intent(context, HomeActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
