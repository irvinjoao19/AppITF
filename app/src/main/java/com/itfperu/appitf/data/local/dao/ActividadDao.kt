package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.Actividad

@Dao
interface ActividadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertActividadTask(c: Actividad)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertActividadListTask(c: List<Actividad>)

    @Update
    fun updateActividadTask(vararg c: Actividad)

    @Delete
    fun deleteActividadTask(c: Actividad)

    @Query("SELECT * FROM Actividad")
    fun getActividad(): LiveData<Actividad>

    @Query("SELECT * FROM Actividad WHERE tipo =:t")
    fun getActividades(t: Int): LiveData<List<Actividad>>

    @Query("SELECT actividadId FROM Actividad")
    fun getActividadId(): Int

    @Query("SELECT * FROM Actividad WHERE actividadId =:id")
    fun getActividadById(id: Int): LiveData<Actividad>

    @Query("DELETE FROM Actividad")
    fun deleteAll()

    @Query("SELECT actividadId FROM Actividad")
    fun getActividadIdTask(): Int

    @Query("SELECT * FROM Actividad WHERE active = 1 AND tipo =:t")
    fun getActividadTask(t: Int): List<Actividad>

    @Query("UPDATE Actividad SET identity =:codigoRetorno ,active =0 WHERE actividadId =:codigoBase")
    fun updateEnabledActividad(codigoBase: Int, codigoRetorno: Int)

    @Query("SELECT * FROM Actividad WHERE actividadId =:id")
    fun getActividadByIdTask(id: Int): Actividad
}