package com.xiaokun.punch;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

import androidx.work.Data;
import androidx.work.WorkManager;
import androidx.work.Worker;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2018/07/20
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class VoiceWorker extends Worker
{
    private static final String TAG = "VoiceWorker";
    public static final long SECOND = 1000;
    public static final String TIME = "time";

    private TextToSpeech mTextToSpeech;

    @NonNull
    @Override
    public Result doWork()
    {
//        try
//        {
//            Thread.sleep(10 * SECOND, 0);
//        } catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }

        Data inputData = getInputData();
        long dataLong = inputData.getLong(TIME, 0);


        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Log.e(TAG, "(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + "执行任务");
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
//        long triggerAtTime = SystemClock.elapsedRealtime() + 10 * SECOND;  20:05:55
        long millis = 0;
        if (dataLong > 0)
        {
            millis = dataLong;
        } else
        {
            millis = System.currentTimeMillis() + 180 * SECOND;
        }
        App.mPref.put(TIME, millis);
//        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        getApplicationContext().startActivity(intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
//            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
        } else
        {
//            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
            alarmManager.set(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
        }

//        Handler handler = new Handler(Looper.getMainLooper());
//        handler.post(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                Log.e(TAG, "(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + "执行任务");
//                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                getApplicationContext().startActivity(intent);
//
//                Toast.makeText(getApplicationContext(), "doworker", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        handler.postDelayed(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                Log.e(TAG, "(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + "执行任务");
//                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                getApplicationContext().startActivity(intent);
//
//                Toast.makeText(getApplicationContext(), "doworker", Toast.LENGTH_SHORT).show();
//
//            }
//        }, 10 * SECOND);

//        //文字转语音
//        mTextToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener()
//        {
//            @Override
//            public void onInit(int status)
//            {
//                int result = mTextToSpeech.setLanguage(Locale.CHINA);
//                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
//                {
//                    Toast.makeText(getApplicationContext(), "数据丢失或不支持", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        mTextToSpeech.speak("下班时间到了,下班时间到了,下班时间到了", TextToSpeech.QUEUE_FLUSH, null);

        return Result.SUCCESS;
    }
}
