package com.xiaokun.punch;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * 考勤表对象
 *
 * @author 肖坤
 * @date 2018/07/16
 */
@Entity(tableName = "punch_books")
public class PunchBook
{
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    public String workTime;

    public String theoryOffTime;
}
