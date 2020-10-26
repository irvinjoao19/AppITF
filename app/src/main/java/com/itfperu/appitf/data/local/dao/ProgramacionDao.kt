package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.Programacion

@Dao
interface ProgramacionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProgramacionTask(c: Programacion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProgramacionListTask(c: List<Programacion>)

    @Update
    fun updateProgramacionTask(vararg c: Programacion)

    @Delete
    fun deleteProgramacionTask(c: Programacion)

    @Query("SELECT * FROM Programacion")
    fun getProgramaciones(): LiveData<List<Programacion>>

    @Query("SELECT * FROM Programacion WHERE estadoProgramacion=:e")
    fun getProgramaciones(e: Int): LiveData<List<Programacion>>

    @Query("SELECT * FROM Programacion WHERE estadoProgramacion=:e AND (cmpMedico LIKE :s OR nombreMedico LIKE :s OR apellidoPMedico LIKE :s OR apellidoMMedico LIKE :s) ")
    fun getProgramaciones(e: Int, s: String): LiveData<List<Programacion>>

    @Query("SELECT * FROM Programacion WHERE usuarioId =:u")
    fun getProgramacionesU(u: Int): LiveData<List<Programacion>>

    @Query("SELECT * FROM Programacion WHERE usuarioId =:u AND active=:e ")
    fun getProgramacionesU(u: Int, e: Int): LiveData<List<Programacion>>

    @Query("SELECT * FROM Programacion WHERE programacionId =:id")
    fun getProgramacionById(id: Int): LiveData<Programacion>

    @Query("DELETE FROM Programacion")
    fun deleteAll()

    @Query("SELECT * FROM Programacion WHERE active = 1")
    fun getProgramacionTask(): List<Programacion>

    @Query("UPDATE Programacion SET identity =:codigoRetorno ,active = 0 WHERE programacionId =:codigoBase")
    fun updateEnabledProgramacion(codigoBase: Int, codigoRetorno: Int)

    @Query("SELECT * FROM Programacion WHERE programacionId =:id")
    fun getProgramacionByIdTask(id: Int): Programacion

    @Query("SELECT * FROM Programacion WHERE programacionId=:id AND active = 1")
    fun getProgramacionOffLineIdTask(id: Int): Programacion
}