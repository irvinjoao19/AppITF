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

    @Query("SELECT * FROM TargetCab WHERE tipoTarget =:t AND tipo =:tt")
    fun getTargetsAltas(t: String, tt: Int): LiveData<List<TargetCab>>

    @Query("SELECT * FROM TargetCab WHERE usuarioId=:u AND fechaInicio=:fi AND fechaFinal=:ff AND tipo =:t AND tipoTarget=:tt")
    fun getTargetsAltas(
        u: Int, fi: String, ff: String, t: Int, tt: String
    ): LiveData<List<TargetCab>>

    @Query("SELECT * FROM TargetCab WHERE usuarioId=:u AND fechaInicio=:fi AND fechaFinal=:ff AND tipo =:t AND estado=:e AND tipoTarget=:tt")
    fun getTargetsAltas(
        u: Int, fi: String, ff: String, e: Int, t: Int, tt: String
    ): LiveData<List<TargetCab>>

    @Query("SELECT * FROM TargetCab WHERE fechaInicio=:fi AND fechaFinal=:ff AND tipo =:t AND tipoTarget=:tt")
    fun getTargetsAltas(
         fi: String, ff: String,t: Int, tt: String
    ): LiveData<List<TargetCab>>

    @Query("SELECT * FROM TargetCab WHERE fechaInicio=:fi AND fechaFinal=:ff AND tipo =:t AND estado=:e AND tipoTarget=:tt")
    fun getTargetsAltas(
        fi: String, ff: String,e:Int,t: Int, tt: String
    ): LiveData<List<TargetCab>>

    @Query("SELECT targetCabId FROM TargetCab ORDER BY targetCabId DESC LIMIT 1")
    fun getMaxIdTargetCab(): LiveData<Int>

    @Query("SELECT * FROM TargetCab WHERE targetCabId =:id")
    fun getTargetCabByIdTask(id: Int): TargetCab

    @Query("SELECT * FROM TargetCab WHERE identity =:id AND active = 1")
    fun getTargetCabOffLineIdTask(id: Int): TargetCab

    @Query("SELECT * FROM TargetCab WHERE tipoTarget =:tipo AND tipo =:t AND active = 1")
    fun getTargetCabTask(tipo: String, t: Int): List<TargetCab>

    @Query("UPDATE TargetCab SET identity =:codigoRetorno, active = 0 WHERE targetCabId =:codigoBase")
    fun updateEnabledTargetCab(codigoBase: Int, codigoRetorno: Int)

    @Query("SELECT * FROM TargetCab WHERE targetCabId =:id")
    fun getTargetById(id: Int): LiveData<TargetCab>

    @Query("UPDATE TargetCab SET active = 1 , usuarioId =:u WHERE targetCabId =:id")
    fun updateForSendTarget(id: Int,u:Int)
}