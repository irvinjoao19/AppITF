package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.StockMantenimiento

@Dao
interface StockMantenimientoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStockMantenimientoTask(c: StockMantenimiento)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStockMantenimientoListTask(c: List<StockMantenimiento>)

    @Update
    fun updateStockMantenimientoTask(vararg c: StockMantenimiento)

    @Delete
    fun deleteStockMantenimientoTask(c: StockMantenimiento)

    @Query("SELECT * FROM StockMantenimiento ORDER BY descripcion,lote ASC")
    fun getStockMantenimiento(): LiveData<List<StockMantenimiento>>

    @Query("DELETE FROM StockMantenimiento")
    fun deleteAll()

}