package com.xiaokun.punch;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener
{

    public static final String DATA_FORMAT = "yyyy-MM-dd HH:mm";
    public static final long HOUR = 60 * 60 * 1000;
    public static final long SECOND = 1000;

    private Button mPunchBtn;
    private TextView mWorkTimeTv;
    private TextView mOffTimeTv;
    private TextToSpeech mTextToSpeech;
    private String mOffTime;
    private MyDatabaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    private Intent mServiceIntent;
    private IntentFilter mIntentFilter;
    private NetworkChangeReceiver mNetworkChangeReceiver;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();

        initReceiver();

        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
    }

    private void initView()
    {
        mPunchBtn = findViewById(R.id.punch_btn);
        mWorkTimeTv = findViewById(R.id.work_time_tv);
        mOffTimeTv = findViewById(R.id.off_time_tv);

        initListener(mPunchBtn);

        //文字转语音
        mTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener()
        {
            @Override
            public void onInit(int status)
            {
                int result = mTextToSpeech.setLanguage(Locale.CHINA);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                {
                    Toast.makeText(HomeActivity.this, "数据丢失或不支持", Toast.LENGTH_SHORT).show();
                }
            }
        });
        initDatabase();
    }

    //创建数据库,并创建考勤表
    private void initDatabase()
    {
        mHelper = new MyDatabaseHelper(this, "APPData.db", null, 1);
        mDatabase = mHelper.getWritableDatabase();
        mDatabase.execSQL(MyDatabaseHelper.CREATE_TABLE);
    }

    private void initReceiver()
    {
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        mNetworkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(mNetworkChangeReceiver, mIntentFilter);
    }

    private void initListener(View... views)
    {
        for (View view : views)
        {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.punch_btn:
                //打卡
                punch();
                break;
            default:
                break;
        }
    }

    //打卡
    private void punch()
    {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATA_FORMAT);
        String workFormat = dateFormat.format(new Date(currentTimeMillis));

        String offFormat = dateFormat.format(new Date(currentTimeMillis + 9 * HOUR));

        //先注释
        int hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minutes = Calendar.getInstance().get(Calendar.MINUTE);
        if (hours > 10 || (hours == 9 && minutes >= 30))
        {
            Toast.makeText(this, "调个休吧,已经迟到了", Toast.LENGTH_SHORT).show();
            return;
        }

        String workTime = "上班时间：" + workFormat;
        //上班打卡时间加上9小时等于下班时间
        mOffTime = "下班时间：" + offFormat;

        mWorkTimeTv.setText(workTime);
        mOffTimeTv.setText(mOffTime);

        mTextToSpeech.speak(mOffTime, TextToSpeech.QUEUE_FLUSH, null);

        insert(workTime, mOffTime);
        //下班之前禁止打卡
        mPunchBtn.setEnabled(false);

        mServiceIntent = new Intent(this, AlarmService.class);
        startService(mServiceIntent);

        startAlarm();
    }

    //往考勤表中添加数据
    private void insert(String workTime, String offTime)
    {
        ContentValues values = new ContentValues();
        values.put("workTime", workTime);
        values.put("theoryOffTime", offTime);
        mDatabase.insert("Attendance", null, values);
    }

    //开启定时语音提醒任务
    private void startAlarm()
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long triggerAtTime = SystemClock.elapsedRealtime() + 9 * SECOND;

        Intent intent = new Intent(this, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
        } else
        {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        Toast.makeText(this, "onNewIntent", Toast.LENGTH_SHORT).show();
        if (intent.getAction() == null)
        {
            mTextToSpeech.speak("下班时间到了,下班时间到了,下班时间到了", TextToSpeech.QUEUE_FLUSH, null);
            mPunchBtn.setEnabled(true);
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mTextToSpeech.stop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mTextToSpeech.shutdown();
        stopService(mServiceIntent);
        unregisterReceiver(mNetworkChangeReceiver);
    }

    @Override
    public void onBackPressed()
    {
        //返回桌面
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    class NetworkChangeReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            int hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int minutes = Calendar.getInstance().get(Calendar.MINUTE);
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info == null || !info.isAvailable())
            {
                return;
            }
            String extraInfo = info.getExtraInfo();
            switch (info.getType())
            {
                case ConnectivityManager.TYPE_WIFI:
                    if (!extraInfo.equals("Navinfo_Guest"))
                    {
                        return;
                    }
                    //由移动网络转变成wifi
                    if ((hours == 8 && minutes > 30) || (hours == 9 && minutes < 30))
                    {
                        //延迟2分钟打卡
                        mHandler.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                punch();
                            }
                        }, 2 * 60 * SECOND);
                    }
                    break;
            }
        }
    }

}
