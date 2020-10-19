package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.TargetInfo

@Dao
interface TargetInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTargetTask(c: TargetInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTargetListTask(c: List<TargetInfo>)

    @Update
    fun updateTargetTask(vararg c: TargetInfo)

    @Delete
    fun deleteTargetTask(c: TargetInfo)

    @Query("SELECT * FROM TargetInfo")
    fun getTarget(): LiveData<TargetInfo>

    @Query("SELECT targetId FROM TargetInfo")
    fun getTargetId(): Int

    @Query("SELECT * FROM TargetInfo WHERE targetId =:id")
    fun getTargetById(id: Int): LiveData<TargetInfo>

    @Query("DELETE FROM TargetInfo")
    fun deleteAll()

    @Query("SELECT targetId FROM TargetInfo")
    fun getTargetIdTask(): Int

    @Query("SELECT * FROM TargetInfo")
    fun getTargets(): LiveData<List<TargetInfo>>

    @Query("SELECT * FROM TargetInfo WHERE targetDetId =:id")
    fun getTargetInfo(id: Int): LiveData<List<TargetInfo>>
}