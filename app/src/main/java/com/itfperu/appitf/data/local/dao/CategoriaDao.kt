package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.itfperu.appitf.data.local.model.Categoria

@Dao
interface CategoriaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategoriaTask(c: Categoria)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategoriaListTask(c: List<Categoria>)

    @Update
    fun updateCategoriaTask(vararg c: Categoria)

    @Delete
    fun deleteCategoriaTask(c: Categoria)

    @Query("SELECT * FROM Categoria")
    fun getCategoria(): LiveData<Categoria>

    @Query("SELECT categoriaId FROM Categoria")
    fun getCategoriaIdTask(): Int

    @Query("SELECT * FROM Categoria")
    fun getCategorias(): LiveData<List<Categoria>>

    @Query("DELETE FROM Categoria")
    fun deleteAll()

    @Query("SELECT * FROM Categoria WHERE categoriaId=:id")
    fun getCategoriaById(id: Int): LiveData<Categoria>
}