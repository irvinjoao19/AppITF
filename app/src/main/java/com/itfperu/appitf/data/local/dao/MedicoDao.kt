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

    @Query("SELECT * FROM Medico WHERE estado =:e")
    fun getMedicosByEstado(e: Int): LiveData<List<Medico>>

    @Query("SELECT * FROM Medico WHERE estado =:e AND usuarioId =:u ORDER BY apellidoP,apellidoM,nombreMedico")
    fun getMedicosByEstado(e: Int, u: Int): LiveData<List<Medico>>

    @Query("SELECT * FROM Medico WHERE cpmMedico=:c AND  identificadorId=:id")
    fun getMedicoCmpIdentificador(c: String, id: Int): Medico

    @Query("SELECT * FROM Medico WHERE medicoSolId =:id")
    fun getMedicoBySolIdTask(id: Int): List<Medico>

    @Query("SELECT * FROM Medico WHERE isSelected=:b")
    fun getMedicoSelected(b: Boolean): List<Medico>

    @Query("UPDATE Medico SET isSelected=:b")
    fun enabledMedicoSelected(b: Boolean)

    @Query("SELECT * FROM Medico WHERE estado = 1 ORDER BY apellidoP,apellidoM,nombreMedico ASC")
    fun getCheckMedicos(): DataSource.Factory<Int, Medico>

    @Query("SELECT * FROM Medico WHERE usuarioId =:u ORDER BY apellidoP,apellidoM,nombreMedico ASC")
    fun getCheckMedicos(u: Int): DataSource.Factory<Int, Medico>

    @Query("SELECT * FROM Medico WHERE (nombreCompleto LIKE :s  OR cpmMedico LIKE :s ) AND estado = 1 ORDER BY apellidoP,apellidoM,nombreMedico ASC")
    fun getCheckMedicos(s: String): DataSource.Factory<Int, Medico>

    @Query("SELECT * FROM Medico WHERE (nombreCompleto LIKE :s  OR cpmMedico LIKE :s )  AND usuarioId =:u ORDER BY apellidoP,apellidoM,nombreMedico ASC")
    fun getCheckMedicos(s: String, u: Int): DataSource.Factory<Int, Medico>

    @Query("UPDATE Medico SET active = 1 WHERE medicoId=:id")
    fun closeMedico(id: Int)

    @Query("SELECT medicoId FROM Medico WHERE medicoSolId =:id")
    fun getMedicosId(id: Int): Int

    @Query("UPDATE Medico SET identity =:codigoRetorno, active = 0 WHERE medicoId =:codigoBase")
    fun updateEnabledMedico(codigoBase: Int, codigoRetorno: Int)

    @Query("SELECT * FROM Medico WHERE identity =:i")
    fun getMedicoOffLineByIdTask(i: Int): Medico

    @Query("DELETE FROM Medico WHERE medicoSolId =:id")
    fun deleteMedicoById(id: Int)
}