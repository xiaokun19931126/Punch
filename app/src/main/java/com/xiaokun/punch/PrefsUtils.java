package com.xiaokun.punch;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Keep;


/**
 * @author yuyh.
 * @date 16/4/9.
 */
@Keep
public class PrefsUtils
{

    private SharedPreferences sp;
    public static final String KEY_PK_HOME = "msg_pk_home";
    public static final String KEY_PK_NEW = "msg_pk_new";

    public PrefsUtils(Context context, String fileName)
    {
        sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    /**
     * *************** get ******************
     */

    public String get(String key, String defValue)
    {
        return sp.getString(key, defValue);
    }

    public boolean get(String key, boolean defValue)
    {
        return sp.getBoolean(key, defValue);
    }

    public float get(String key, float defValue)
    {
        return sp.getFloat(key, defValue);
    }

    public int getInt(String key, int defValue)
    {
        return sp.getInt(key, defValue);
    }

    public long get(String key, long defValue)
    {
        return sp.getLong(key, defValue);
    }

    public void put(String key, String value)
    {
        if (value == null)
        {
            sp.edit().remove(key).commit();
        } else
        {
            sp.edit().putString(key, value).commit();
        }
    }

    public void put(String key, boolean value)
    {
        sp.edit().putBoolean(key, value).commit();
    }

    public void put(String key, float value)
    {
        sp.edit().putFloat(key, value).commit();
    }

    public void put(String key, long value)
    {
        sp.edit().putLong(key, value).commit();
    }

    public void putInt(String key, int value)
    {
        sp.edit().putInt(key, value).commit();
    }

    public void clear()
    {
        sp.edit().clear().commit();
    }
}
