package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.Usuario

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsuarioTask(c: Usuario)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsuarioListTask(c: List<Usuario>)

    @Update
    fun updateUsuarioTask(vararg c: Usuario)

    @Delete
    fun deleteUsuarioTask(c: Usuario)

    @Query("SELECT * FROM Usuario")
    fun getUsuario(): LiveData<Usuario>

    @Query("SELECT * FROM Usuario")
    fun getUsuarioTask(): Usuario

    @Query("SELECT usuarioId FROM Usuario")
    fun getUsuarioId(): Int

    @Query("SELECT * FROM Usuario WHERE usuarioId =:id")
    fun getUsuarioById(id: Int): LiveData<Usuario>

    @Query("SELECT * FROM Nombre ")
    fun getUsuarioNombre(): String

    @Query("DELETE FROM Usuario")
    fun deleteAll()

    @Query("SELECT usuarioId FROM Usuario")
    fun getUsuarioIdTask(): Int

    @Query("SELECT * FROM Nombre ")
    fun getNombreUsuario(): LiveData<String>
}