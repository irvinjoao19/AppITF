package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.itfperu.appitf.data.local.model.Personal

@Dao
interface PersonalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPersonalTask(c: Personal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPersonalListTask(c: List<Personal>)

    @Update
    fun updatePersonalTask(vararg c: Personal)

    @Delete
    fun deletePersonalTask(c: Personal)

    @Query("SELECT * FROM Personal")
    fun getPersonal(): LiveData<Personal>

    @Query("SELECT personalId FROM Personal")
    fun getPersonalIdTask(): Int

    @Query("SELECT * FROM Personal")
    fun getPersonals(): LiveData<List<Personal>>

    @Query("DELETE FROM Personal")
    fun deleteAll()

    @Query("SELECT * FROM Personal WHERE personalId=:id")
    fun getPersonalById(id: Int): LiveData<Personal>

    @Query("SELECT * FROM Personal WHERE esSupervisorId = 1 AND estado = 1 ")
    fun getSupervisores(): LiveData<List<Personal>>
}