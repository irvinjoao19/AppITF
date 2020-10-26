package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.itfperu.appitf.data.local.model.Medico

@Dao
interface MedicoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMedicoTask(c: Medico)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMedicoListTask(c: List<Medico>)

    @Update
    fun updateMedicoTask(vararg c: Medico)

    @Delete
    fun deleteMedicoTask(c: Medico)

    @Query("SELECT * FROM Medico")
    fun getMedico(): LiveData<Medico>

    @Query("SELECT usuarioId FROM Medico")
    fun getMedicoId(): Int

    @Query("SELECT * FROM Medico WHERE medicoId =:id")
    fun getMedicoById(id: Int): LiveData<Medico>

    @Query("DELETE FROM Medico")
    fun deleteAll()

    @Query("SELECT usuarioId FROM Medico")
    fun getMedicoIdTask(): Int

    @Query("SELECT * FROM Medico WHERE medicoId =:id")
    fun getMedicoByIdTask(id: Int): Medico

    @Query("SELECT * FROM Medico WHERE medicoSolId =:id")
    fun getMedicosById(id: Int): LiveData<List<Medico>>

    @Query("SELECT medicoId FROM Medico ORDER BY medicoId DESC LIMIT 1")
    fun getMaxIdMedico(): LiveData<Int>

    @Query("SELECT * FROM Medico")
    fun getMedicos(): LiveData<List<Medico>>

    @Query("SELECT * FROM Medico WHERE cpmMedico=:c AND  identificadorId=:id")
    fun getMedicoCmpIdentificador(c: String, id: Int): Medico

    @Query("SELECT * FROM Medico WHERE medicoSolId =:id AND active = 1")
    fun getMedicoBySolIdTask(id: Int): List<Medico>

    @Query("SELECT * FROM Medico WHERE isSelected=:b")
    fun getMedicoSelected(b: Boolean): List<Medico>

    @Query("UPDATE Medico SET isSelected=:b")
    fun enabledMedicoSelected(b: Boolean)

    @Query("SELECT * FROM Medico")
    fun getCheckMedicos(): DataSource.Factory<Int, Medico>

    @Query("SELECT * FROM Medico WHERE usuarioId =:u")
    fun getCheckMedicos(u:Int): DataSource.Factory<Int, Medico>

    @Query("SELECT * FROM Medico WHERE (nombreMedico LIKE :s OR apellidoP LIKE :s OR apellidoM LIKE :s OR cpmMedico LIKE :s )")
    fun getCheckMedicos(s: String): DataSource.Factory<Int, Medico>

    @Query("SELECT * FROM Medico WHERE (nombreMedico LIKE :s OR apellidoP LIKE :s OR apellidoM LIKE :s OR cpmMedico LIKE :s )  AND usuarioId =:u ")
    fun getCheckMedicos(s: String,u:Int): DataSource.Factory<Int, Medico>

    @Query("UPDATE Medico SET active = 1 WHERE medicoId=:id")
    fun closeMedico(id: Int)

    @Query("SELECT count(*) FROM Medico WHERE active = 2 AND medicoSolId =:id")
    fun getMedicosInactivos(id: Int): Int

    @Query("UPDATE Medico SET identity =:codigoRetorno, active = 0 WHERE medicoId =:codigoBase")
    fun updateEnabledMedico(codigoBase: Int, codigoRetorno: Int)
}