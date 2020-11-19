package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.itfperu.appitf.data.local.model.Ciclo

@Dao
interface CicloDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCicloTask(c: Ciclo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCicloListTask(c: List<Ciclo>)

    @Update
    fun updateCicloTask(vararg c: Ciclo)

    @Delete
    fun deleteCicloTask(c: Ciclo)

    @Query("SELECT * FROM Ciclo")
    fun getCiclo(): LiveData<Ciclo>

    @Query("SELECT cicloId FROM Ciclo")
    fun getCicloIdTask(): Int

    @Query("SELECT * FROM Ciclo")
    fun getCiclos(): LiveData<List<Ciclo>>

    @Query("DELETE FROM Ciclo")
    fun deleteAll()

    @Query("SELECT * FROM Ciclo WHERE cicloId=:id")
    fun getCicloById(id: Int): LiveData<Ciclo>

    @Query("SELECT * FROM Ciclo WHERE estado=:e")
    fun getCicloProceso(e: Int): Ciclo

    @Query("SELECT * FROM Ciclo WHERE estado=4")
    fun getCicloProceso(): LiveData<List<Ciclo>>

    @Query("SELECT * FROM Ciclo WHERE estado=4")
    fun getFirstCicloProceso(): LiveData<Ciclo>

}