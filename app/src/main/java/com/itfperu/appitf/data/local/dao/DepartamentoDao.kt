package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.Departamento

@Dao
interface DepartamentoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDepartamentoTask(c: Departamento)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDepartamentoListTask(c: List<Departamento>)

    @Update
    fun updateDepartamentoTask(vararg c: Departamento)

    @Delete
    fun deleteDepartamentoTask(c: Departamento)

    @Query("SELECT * FROM Departamento")
    fun getDepartamentos(): LiveData<List<Departamento>>

    @Query("DELETE FROM Departamento")
    fun deleteAll()
}