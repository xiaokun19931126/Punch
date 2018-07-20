package com.xiaokun.punch;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * DAO :data access object
 * 数据访问对象
 *
 * @author 肖坤
 * @date 2018/07/16
 */

@Dao
public interface PunchBookDao
{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPunch(PunchBook punchBook);

    @Query("SELECT * FROM punch_books")
    LiveData<List<PunchBook>> findAllPunches();

//    @Query("SELECT * FROM punch_books")
//    List<PunchBook> findAllPunches();

}
