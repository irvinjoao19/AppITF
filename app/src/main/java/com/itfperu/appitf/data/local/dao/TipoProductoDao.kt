package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.itfperu.appitf.data.local.model.TipoProducto

@Dao
interface TipoProductoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTipoProductoTask(c: TipoProducto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTipoProductoListTask(c: List<TipoProducto>)

    @Update
    fun updateTipoProductoTask(vararg c: TipoProducto)

    @Delete
    fun deleteTipoProductoTask(c: TipoProducto)

    @Query("SELECT * FROM TipoProducto")
    fun getTipoProducto(): LiveData<TipoProducto>

    @Query("SELECT tipoProductoId FROM TipoProducto")
    fun getTipoProductoIdTask(): Int

    @Query("SELECT * FROM TipoProducto")
    fun getTipoProductos(): LiveData<List<TipoProducto>>

    @Query("SELECT * FROM TipoProducto WHERE estadoId = 1")
    fun getTipoProductoActive(): LiveData<List<TipoProducto>>

    @Query("DELETE FROM TipoProducto")
    fun deleteAll()

    @Query("SELECT * FROM TipoProducto WHERE tipoProductoId=:id")
    fun getTipoProductoById(id: Int): LiveData<TipoProducto>

    @Query("SELECT * FROM TipoProducto WHERE codigo =:c")
    fun getTipoProductoByCod(c: String): TipoProducto
}