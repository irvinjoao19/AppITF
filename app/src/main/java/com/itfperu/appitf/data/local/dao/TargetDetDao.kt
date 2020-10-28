package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.TargetDet

@Dao
interface TargetDetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTargetDetTask(c: TargetDet)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTargetDetListTask(c: List<TargetDet>)

    @Update
    fun updateTargetDetTask(vararg c: TargetDet)

    @Delete
    fun deleteTargetDetTask(c: TargetDet)

    @Query("SELECT * FROM TargetDet")
    fun getTargetDet(): LiveData<TargetDet>

    @Query("SELECT targetCabId FROM TargetDet")
    fun getTargetDetId(): Int

    @Query("SELECT * FROM TargetDet WHERE targetCabId =:id")
    fun getTargetDetById(id: Int): LiveData<List<TargetDet>>

    @Query("DELETE FROM TargetDet")
    fun deleteAll()

    @Query("SELECT * FROM TargetDet WHERE targetCabId=:id")
    fun getTargetDetIdTask(id: Int): List<TargetDet>

    @Query("SELECT * FROM TargetDet")
    fun getTargetsAltas(): LiveData<List<TargetDet>>

    @Query("SELECT targetDetId FROM TargetDet ORDER BY targetDetId DESC LIMIT 1")
    fun getMaxIdTargetDet(): LiveData<Int>

    @Query("SELECT * FROM TargetDet WHERE targetDetId =:id")
    fun getTargetDetByIdTask(id: Int): TargetDet

    @Query("SELECT * FROM TargetDet WHERE medicoId=:m AND targetCabId =:c")
    fun getMedicoExits(m: Int, c: Int): Boolean

    @Query("DELETE FROM TargetDet WHERE targetCabId=:id")
    fun deleteTargetDet(id: Int)

    @Query("SELECT * FROM TargetDet WHERE identity =:id")
    fun getTargetDetOffLineByIdTask(id: Int): TargetDet

    @Query("UPDATE TargetDet SET identity =:codigoRetorno WHERE targetDetId=:codigoBase")
    fun updateEnabledTargetDet(codigoBase: Int, codigoRetorno: Int)
}