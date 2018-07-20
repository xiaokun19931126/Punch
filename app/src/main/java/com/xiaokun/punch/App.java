package com.xiaokun.punch;

import android.app.Application;

import tech.linjiang.pandora.Pandora;

/**
 * @author 肖坤
 * @date 2018/07/16
 */
public class App extends Application
{
    private static App sApp;
    public static PrefsUtils mPref;

    @Override
    public void onCreate()
    {
        super.onCreate();
        sApp = this;
        mPref = new PrefsUtils(this, "punch_sp");
        Pandora.init(this).enableShakeOpen();
    }

    public static Application getApplication()
    {
        return sApp;
    }
}
