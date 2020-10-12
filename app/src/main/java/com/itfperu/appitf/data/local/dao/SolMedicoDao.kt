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

    @Query("SELECT * FROM SolMedico WHERE tipo =:t")
    fun getMedicos(t: Int): LiveData<List<SolMedico>>

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

    @Query("UPDATE SolMedico SET identity =:codigoRetorno, estado = 2 WHERE solMedicoId =:codigoBase")
    fun updateEnabledMedico(codigoBase: Int, codigoRetorno: Int)
}