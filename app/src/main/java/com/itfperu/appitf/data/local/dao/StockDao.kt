package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.Stock

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStockTask(c: Stock)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStockListTask(c: List<Stock>)

    @Update
    fun updateStockTask(vararg c: Stock)

    @Delete
    fun deleteStockTask(c: Stock)

    @Query("SELECT * FROM Stock ORDER BY descripcion ASC")
    fun getStocks(): LiveData<List<Stock>>

    @Query("SELECT * FROM Stock WHERE codigoProducto=:cod")
    fun getStockById(cod: String): LiveData<Stock>

    @Query("DELETE FROM Stock")
    fun deleteAll()
}