package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.NuevaDireccion

@Dao
interface NuevaDireccionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNuevaDireccionTask(c: NuevaDireccion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNuevaDireccionListTask(c: List<NuevaDireccion>)

    @Update
    fun updateNuevaDireccionTask(vararg c: NuevaDireccion)

    @Delete
    fun deleteNuevaDireccionTask(c: NuevaDireccion)

    @Query("SELECT * FROM NuevaDireccion")
    fun getNuevaDireccion(): LiveData<NuevaDireccion>

    @Query("SELECT usuarioId FROM NuevaDireccion")
    fun getNuevaDireccionId(): Int

    @Query("SELECT * FROM NuevaDireccion")
    fun getNuevaDirecciones(): LiveData<List<NuevaDireccion>>

    @Query("SELECT * FROM NuevaDireccion WHERE fechaInicio=:fi AND fechaFinal =:ff AND estadoId =:e AND tipo =:t")
    fun getNuevaDirecciones(fi: String, ff: String, e: Int, t: Int): LiveData<List<NuevaDireccion>>

    @Query("SELECT * FROM NuevaDireccion WHERE fechaInicio=:fi AND fechaFinal =:ff AND tipo =:t")
    fun getNuevaDirecciones(fi: String, ff: String, t: Int): LiveData<List<NuevaDireccion>>

    @Query("DELETE FROM NuevaDireccion")
    fun deleteAll()

    @Query("SELECT usuarioId FROM NuevaDireccion")
    fun getNuevaDireccionIdTask(): Int

    @Query("SELECT * FROM NuevaDireccion WHERE solDireccionId =:id")
    fun getNuevaDireccionById(id: Int): NuevaDireccion

    @Query("UPDATE NuevaDireccion SET identity =:codigoRetorno ,medicoDireccionId=:codigoAlterno , active = 0  WHERE solDireccionId =:codigoBase")
    fun updateEnabledDireccion(codigoBase: Int, codigoRetorno: Int, codigoAlterno: Int)

    @Query("SELECT * FROM NuevaDireccion WHERE identity =:id")
    fun getNuevaDireccionOffLine(id: Int): NuevaDireccion

    @Query("SELECT * FROM NuevaDireccion WHERE tipo=:t AND active = 1")
    fun getNuevaDireccionTask(t: Int): List<NuevaDireccion>

    @Query("SELECT solDireccionId FROM NuevaDireccion ORDER BY solDireccionId DESC LIMIT 1")
    fun getNuevaDireccionMaxId(): LiveData<Int>

    @Query("SELECT * FROM NuevaDireccion WHERE solDireccionId =:id")
    fun getNuevaDireccionId(id:Int) : LiveData<NuevaDireccion>
}