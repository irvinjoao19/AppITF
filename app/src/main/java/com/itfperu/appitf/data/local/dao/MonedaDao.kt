package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.itfperu.appitf.data.local.model.Moneda

@Dao
interface MonedaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMonedaTask(c: Moneda)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMonedaListTask(c: List<Moneda>)

    @Update
    fun updateMonedaTask(vararg c: Moneda)

    @Delete
    fun deleteMonedaTask(c: Moneda)

    @Query("SELECT * FROM Moneda")
    fun getMoneda(): LiveData<Moneda>

    @Query("SELECT monedaId FROM Moneda")
    fun getMonedaIdTask(): Int

    @Query("SELECT * FROM Moneda")
    fun getMonedaes(): LiveData<List<Moneda>>

    @Query("DELETE FROM Moneda")
    fun deleteAll()

    @Query("SELECT * FROM Moneda WHERE monedaId=:id")
    fun getMonedaById(id: Int): LiveData<Moneda>
}