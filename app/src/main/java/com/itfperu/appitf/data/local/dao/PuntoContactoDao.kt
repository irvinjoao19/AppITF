package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.PuntoContacto

@Dao
interface PuntoContactoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPuntoContactoTask(c: PuntoContacto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPuntoContactoListTask(c: List<PuntoContacto>)

    @Update
    fun updatePuntoContactoTask(vararg c: PuntoContacto)

    @Delete
    fun deletePuntoContactoTask(c: PuntoContacto)

    @Query("SELECT * FROM PuntoContacto")
    fun getPuntoContacto(): LiveData<PuntoContacto>

    @Query("SELECT usuarioId FROM PuntoContacto")
    fun getPuntoContactoId(): Int

    @Query("SELECT * FROM PuntoContacto WHERE usuarioId =:id")
    fun getPuntoContactoById(id: Int): LiveData<List<PuntoContacto>>

    @Query("DELETE FROM PuntoContacto")
    fun deleteAll()

    @Query("SELECT usuarioId FROM PuntoContacto")
    fun getPuntoContactoIdTask(): Int

    @Query("SELECT * FROM PuntoContacto WHERE fechaPuntoContacto =:f")
    fun getPuntoContactos(f: String): LiveData<List<PuntoContacto>>

    @Query("SELECT * FROM PuntoContacto WHERE puntoContactoId =:id")
    fun getPuntoContactoOffLine(id: Int): PuntoContacto

    @Query("SELECT * FROM PuntoContacto WHERE active = 1")
    fun getPuntoContactoTask(): List<PuntoContacto>

    @Query("UPDATE PuntoContacto SET active = 0 WHERE puntoContactoId =:codigoBase")
    fun updateEnabledPuntoContacto(codigoBase: Int)
}