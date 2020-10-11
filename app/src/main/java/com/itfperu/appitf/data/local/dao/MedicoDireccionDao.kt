package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.MedicoDireccion

@Dao
interface MedicoDireccionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMedicoDireccionTask(c: MedicoDireccion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMedicoDireccionListTask(c: List<MedicoDireccion>)

    @Update
    fun updateMedicoDireccionTask(vararg c: MedicoDireccion)

    @Delete
    fun deleteMedicoDireccionTask(c: MedicoDireccion)

    @Query("SELECT * FROM MedicoDireccion")
    fun getMedicoDireccion(): LiveData<MedicoDireccion>

    @Query("SELECT medicoDireccionId FROM MedicoDireccion")
    fun getMedicoDireccionId(): Int

    @Query("SELECT * FROM MedicoDireccion WHERE medicoId =:id")
    fun getMedicoDireccionesById(id: Int): LiveData<List<MedicoDireccion>>

    @Query("SELECT * FROM MedicoDireccion WHERE medicoDireccionId =:id")
    fun getDireccionById(id: Int): LiveData<MedicoDireccion>

    @Query("DELETE FROM MedicoDireccion")
    fun deleteAll()

    @Query("SELECT usuarioId FROM MedicoDireccion")
    fun getMedicoDireccionIdTask(): Int

    @Query("DELETE FROM MedicoDireccion WHERE medicoId =:id")
    fun deleteDireccionesById(id: Int)

    @Query("SELECT * FROM MedicoDireccion WHERE medicoId =:id AND active = 1")
    fun getDireccionIdTask(id: Int): List<MedicoDireccion>

    @Query("SELECT COUNT(*) FROM MedicoDireccion WHERE medicoId =:id")
    fun getMedicoDirecciones(id: Int): Int

    @Query("UPDATE MedicoDireccion SET identity =:codigoRetorno, active = 0 WHERE medicoDireccionId =:codigoBase")
    fun updateEnabledDireccion(codigoBase: Int, codigoRetorno: Int)
}