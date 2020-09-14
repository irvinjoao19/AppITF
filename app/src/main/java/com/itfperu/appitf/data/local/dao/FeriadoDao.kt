package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.itfperu.appitf.data.local.model.Feriado

@Dao
interface FeriadoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFeriadoTask(c: Feriado)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFeriadoListTask(c: List<Feriado>)

    @Update
    fun updateFeriadoTask(vararg c: Feriado)

    @Delete
    fun deleteFeriadoTask(c: Feriado)

    @Query("SELECT * FROM Feriado")
    fun getFeriado(): LiveData<Feriado>

    @Query("SELECT feriadoId FROM Feriado")
    fun getFeriadoIdTask(): Int

    @Query("SELECT * FROM Feriado")
    fun getFeriadoes(): LiveData<List<Feriado>>

    @Query("DELETE FROM Feriado")
    fun deleteAll()
}