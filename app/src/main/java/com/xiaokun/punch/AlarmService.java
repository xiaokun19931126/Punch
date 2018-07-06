package com.xiaokun.punch;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

/**
 * 前台服务,开启震动通知。防止锁频条件下,语音调不起来
 */
public class AlarmService extends Service
{
    public static final long HOUR = 60 * 60 * 1000;
    public static final long SECOND = 1000;
    private static final String KEY_ALARM_INTENT = "ALARM_INTENT";
    public static final String FOREGROUND = "foreground_service";
    public static final String MINUTE_NOTICE = "MINUTE_NOTICE";

    public AlarmService()
    {
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        //开启前台服务,保证服务不被杀死
        Notification notification = new NotificationCompat.Builder(this, HomeActivity.CHANNEL_ID)
                .setContentTitle("下班提醒前台服务")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                .build();
        startForeground(1, notification);

        //启动定时通知任务,提前一分钟
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long triggerAtTime = SystemClock.elapsedRealtime() + 9 * HOUR - 60 * SECOND;

        Intent intent = new Intent(this, AlarmService.class);
        intent.putExtra(KEY_ALARM_INTENT, true);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
        } else
        {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if (intent != null && intent.getBooleanExtra(KEY_ALARM_INTENT, false))
        {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            //开启通知,带有振动通知
            Notification notification = new NotificationCompat.Builder(this, HomeActivity.CHANNEL_ID)
                    .setContentText("还有一分钟就下班啦")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                    .setWhen(System.currentTimeMillis())
                    .setVibrate(new long[]{0, 1000, 1000, 1000})
                    .setLights(Color.GREEN, 1000, 1000)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setAutoCancel(true)
                    .build();
            manager.notify(2, notification);
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
