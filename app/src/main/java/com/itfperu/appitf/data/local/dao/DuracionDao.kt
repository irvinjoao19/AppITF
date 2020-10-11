package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.Duracion

@Dao
interface DuracionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDuracionTask(c: Duracion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDuracionListTask(c: List<Duracion>)

    @Update
    fun updateDuracionTask(vararg c: Duracion)

    @Delete
    fun deleteDuracionTask(c: Duracion)

    @Query("SELECT * FROM Duracion")
    fun getDuracions(): LiveData<List<Duracion>>

    @Query("DELETE FROM Duracion")
    fun deleteAll()
}