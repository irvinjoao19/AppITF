package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.itfperu.appitf.data.local.model.Producto

@Dao
interface ProductoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductoTask(c: Producto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductoListTask(c: List<Producto>)

    @Update
    fun updateProductoTask(vararg c: Producto)

    @Delete
    fun deleteProductoTask(c: Producto)

    @Query("SELECT * FROM Producto")
    fun getProducto(): LiveData<Producto>

    @Query("SELECT productoId FROM Producto")
    fun getProductoIdTask(): Int

    @Query("SELECT * FROM Producto")
    fun getProductos(): LiveData<List<Producto>>

    @Query("DELETE FROM Producto")
    fun deleteAll()

    @Query("SELECT * FROM Producto WHERE productoId=:id")
    fun getProductoById(id: Int): LiveData<Producto>
}