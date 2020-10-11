package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.Provincia

@Dao
interface ProvinciaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProvinciaTask(c: Provincia)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProvinciaListTask(c: List<Provincia>)

    @Update
    fun updateProvinciaTask(vararg c: Provincia)

    @Delete
    fun deleteProvinciaTask(c: Provincia)

    @Query("SELECT * FROM Provincia")
    fun getProvincias(): LiveData<List<Provincia>>

    @Query("DELETE FROM Provincia")
    fun deleteAll()
}