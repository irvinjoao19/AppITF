package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.Identificador

@Dao
interface IdentificadorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIdentificadorTask(c: Identificador)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIdentificadorListTask(c: List<Identificador>)

    @Update
    fun updateIdentificadorTask(vararg c: Identificador)

    @Delete
    fun deleteIdentificadorTask(c: Identificador)

    @Query("SELECT * FROM Identificador")
    fun getIdentificadores(): LiveData<List<Identificador>>

    @Query("SELECT identificadorId FROM Identificador")
    fun getIdentificadorId(): Int

    @Query("SELECT * FROM Identificador WHERE identificadorId =:id")
    fun getIdentificadorById(id: Int): LiveData<List<Identificador>>

    @Query("DELETE FROM Identificador")
    fun deleteAll()

    @Query("SELECT identificadorId FROM Identificador")
    fun getIdentificadorIdTask(): Int
}