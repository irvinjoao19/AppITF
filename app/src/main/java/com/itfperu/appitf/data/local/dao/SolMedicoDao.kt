package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.SolMedico

@Dao
interface SolMedicoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMedicoTask(c: SolMedico)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMedicoListTask(c: List<SolMedico>)

    @Update
    fun updateMedicoTask(vararg c: SolMedico)

    @Delete
    fun deleteMedicoTask(c: SolMedico)

    @Query("SELECT * FROM SolMedico")
    fun getMedico(): LiveData<SolMedico>

    @Query("SELECT * FROM SolMedico WHERE usuarioId =:u AND fechaInicio=:fi AND fechaFinal=:ff AND estadoSol=:e AND tipo =:t")
    fun getMedicos(u: Int, fi: String, ff: String, e: Int, t: Int): LiveData<List<SolMedico>>

    @Query("SELECT * FROM SolMedico WHERE usuarioId =:u AND fechaInicio=:fi AND fechaFinal=:ff  AND tipo =:t")
    fun getMedicos(u: Int, fi: String, ff: String, t: Int): LiveData<List<SolMedico>>

    @Query("SELECT * FROM SolMedico WHERE fechaInicio=:fi AND fechaFinal=:ff AND tipo =:t")
    fun getMedicos(fi: String, ff: String, t: Int): LiveData<List<SolMedico>>

    @Query("SELECT * FROM SolMedico WHERE fechaInicio=:fi AND fechaFinal=:ff AND estadoSol=:e AND tipo =:t")
    fun getMedicos(fi: String, ff: String, e: Int, t: Int): LiveData<List<SolMedico>>

    @Query("SELECT solMedicoId FROM SolMedico")
    fun getMedicoId(): Int

    @Query("SELECT * FROM SolMedico WHERE solMedicoId =:id")
    fun getMedicoById(id: Int): LiveData<SolMedico>

    @Query("DELETE FROM SolMedico")
    fun deleteAll()

    @Query("SELECT solMedicoId FROM SolMedico")
    fun getMedicoIdTask(): Int

    @Query("SELECT solMedicoId FROM SolMedico ORDER BY solMedicoId DESC LIMIT 1")
    fun getMaxIdSolMedico(): LiveData<Int>

    @Query("SELECT * FROM SolMedico WHERE solMedicoId =:id")
    fun getSolMedicoByIdTask(id: Int): SolMedico

    @Query("SELECT * FROM SolMedico WHERE estado = 1 AND tipo =:t")
    fun getSolMedicoTask(t: Int): List<SolMedico>

    @Query("UPDATE SolMedico SET identity =:codigoRetorno, estado = 0 WHERE solMedicoId =:codigoBase")
    fun updateEnabledMedico(codigoBase: Int, codigoRetorno: Int)

    @Query("SELECT * FROM SolMedico WHERE solMedicoId =:id")
    fun getSolMedicoCab(id: Int): LiveData<SolMedico>

    @Query("SELECT * FROM SolMedico WHERE solMedicoId =:id AND estado = 1")
    fun getSolMedicoOffLineIdTask(id: Int): SolMedico
}