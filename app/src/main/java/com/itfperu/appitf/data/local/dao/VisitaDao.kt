package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.itfperu.appitf.data.local.model.Visita

@Dao
interface VisitaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVisitaTask(c: Visita)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVisitaListTask(c: List<Visita>)

    @Update
    fun updateVisitaTask(vararg c: Visita)

    @Delete
    fun deleteVisitaTask(c: Visita)

    @Query("SELECT * FROM Visita")
    fun getVisita(): LiveData<Visita>

    @Query("SELECT visitaId FROM Visita")
    fun getVisitaIdTask(): Int

    @Query("SELECT * FROM Visita")
    fun getVisitas(): LiveData<List<Visita>>

    @Query("DELETE FROM Visita")
    fun deleteAll()

    @Query("SELECT * FROM Visita WHERE visitaId=:id")
    fun getVisitaById(id: Int): LiveData<Visita>
}