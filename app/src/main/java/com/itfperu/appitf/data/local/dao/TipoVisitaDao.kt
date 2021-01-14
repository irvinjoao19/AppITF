package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.TipoVisita

@Dao
interface TipoVisitaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTipoVisitaTask(c: TipoVisita)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTipoVisitaListTask(c: List<TipoVisita>)

    @Update
    fun updateTipoVisitaTask(vararg c: TipoVisita)

    @Delete
    fun deleteTipoVisitaTask(c: TipoVisita)

    @Query("SELECT * FROM TipoVisita")
    fun getTipoVisita(): LiveData<TipoVisita>

    @Query("SELECT * FROM TipoVisita")
    fun getTipoVisitas(): LiveData<List<TipoVisita>>

    @Query("DELETE FROM TipoVisita")
    fun deleteAll()

    @Query("SELECT * FROM TipoVisita WHERE tipoVisitaId =:id")
    fun getTipoVisitaById(id: Int): LiveData<TipoVisita>
}