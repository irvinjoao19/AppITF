package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.TargetCab

@Dao
interface TargetCabDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTargetCabTask(c: TargetCab)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTargetCabListTask(c: List<TargetCab>)

    @Update
    fun updateTargetCabTask(vararg c: TargetCab)

    @Delete
    fun deleteTargetCabTask(c: TargetCab)

    @Query("SELECT * FROM TargetCab")
    fun getTargetCab(): LiveData<TargetCab>

    @Query("SELECT targetCabId FROM TargetCab")
    fun getTargetCabId(): Int

    @Query("SELECT * FROM TargetCab WHERE targetCabId =:id")
    fun getTargetCabById(id: Int): LiveData<List<TargetCab>>

    @Query("DELETE FROM TargetCab")
    fun deleteAll()

    @Query("SELECT targetCabId FROM TargetCab")
    fun getTargetCabIdTask(): Int

    @Query("SELECT * FROM TargetCab WHERE tipoTarget =:t")
    fun getTargetsAltas(t: String): LiveData<List<TargetCab>>

    @Query("SELECT targetCabId FROM TargetCab ORDER BY targetCabId DESC LIMIT 1")
    fun getMaxIdTargetCab(): LiveData<Int>

    @Query("SELECT * FROM TargetCab WHERE targetCabId =:id")
    fun getTargetCabByIdTask(id: Int): TargetCab

    @Query("SELECT * FROM TargetCab WHERE tipoTarget =:tipo AND active = 1")
    fun getTargetCabTask(tipo:String): List<TargetCab>

    @Query("UPDATE TargetCab SET identity =:codigoRetorno, active = 2 WHERE targetCabId =:codigoBase")
    fun updateEnabledTargetCab(codigoBase: Int, codigoRetorno: Int)
}