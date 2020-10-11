package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.TargetM

@Dao
interface TargetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTargetTask(c: TargetM)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTargetListTask(c: List<TargetM>)

    @Update
    fun updateTargetTask(vararg c: TargetM)

    @Delete
    fun deleteTargetTask(c: TargetM)

    @Query("SELECT * FROM TargetM")
    fun getTarget(): LiveData<TargetM>

    @Query("SELECT targetId FROM TargetM")
    fun getTargetId(): Int

    @Query("SELECT * FROM TargetM WHERE targetId =:id")
    fun getTargetById(id: Int): LiveData<TargetM>

    @Query("DELETE FROM TargetM")
    fun deleteAll()

    @Query("SELECT targetId FROM TargetM")
    fun getTargetIdTask(): Int

    @Query("SELECT * FROM TargetM")
    fun getTargets(): LiveData<List<TargetM>>
}