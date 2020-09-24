package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.itfperu.appitf.data.local.model.Perfil

@Dao
interface PerfilDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPerfilTask(c: Perfil)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPerfilListTask(c: List<Perfil>)

    @Update
    fun updatePerfilTask(vararg c: Perfil)

    @Delete
    fun deletePerfilTask(c: Perfil)

    @Query("SELECT * FROM Perfil")
    fun getPerfil(): LiveData<Perfil>

    @Query("SELECT perfilId FROM Perfil")
    fun getPerfilIdTask(): Int

    @Query("SELECT * FROM Perfil")
    fun getPerfiles(): LiveData<List<Perfil>>

    @Query("SELECT * FROM Perfil WHERE estadoId = 1")
    fun getPerfilsActive(): LiveData<List<Perfil>>
//    fun getPerfiles(id: Int): DataSource.Factory<Int,Perfil>

    @Query("DELETE FROM Perfil")
    fun deleteAll()

    @Query("SELECT * FROM Perfil WHERE perfilId=:id")
    fun getPerfilById(id: Int): LiveData<Perfil>

    @Query("SELECT * FROM Perfil WHERE codigo=:c")
    fun getPerfilByCod(c: String): Perfil
}