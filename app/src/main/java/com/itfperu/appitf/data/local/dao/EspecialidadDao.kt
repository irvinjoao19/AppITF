package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.itfperu.appitf.data.local.model.Especialidad

@Dao
interface EspecialidadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEspecialidadTask(c: Especialidad)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEspecialidadListTask(c: List<Especialidad>)

    @Update
    fun updateEspecialidadTask(vararg c: Especialidad)

    @Delete
    fun deleteEspecialidadTask(c: Especialidad)

    @Query("SELECT * FROM Especialidad")
    fun getEspecialidad(): LiveData<Especialidad>

    @Query("SELECT especialidadId FROM Especialidad")
    fun getEspecialidadIdTask(): Int

    @Query("SELECT * FROM Especialidad")
    fun getEspecialidades(): LiveData<List<Especialidad>>

    @Query("DELETE FROM Especialidad")
    fun deleteAll()

    @Query("SELECT * FROM Especialidad WHERE especialidadId=:id")
    fun getEspecialidadById(id: Int): LiveData<Especialidad>

    @Query("SELECT * FROM Especialidad WHERE codigo=:c")
    fun getEspecialidadByCod(c: String): Especialidad
}