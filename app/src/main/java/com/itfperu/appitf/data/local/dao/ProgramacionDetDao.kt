package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.ProgramacionDet

@Dao
interface ProgramacionDetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProgramacionDetTask(c: ProgramacionDet)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProgramacionDetListTask(c: List<ProgramacionDet>)

    @Update
    fun updateProgramacionDetTask(vararg c: ProgramacionDet)

    @Delete
    fun deleteProgramacionDetTask(c: ProgramacionDet)

    @Query("SELECT * FROM ProgramacionDet WHERE programacionId=:id")
    fun getProgramacionesById(id: Int): LiveData<List<ProgramacionDet>>

    @Query("SELECT * FROM ProgramacionDet WHERE programacionDetId=:id")
    fun getProgramacionById(id: Int): LiveData<ProgramacionDet>

    @Query("DELETE FROM ProgramacionDet")
    fun deleteAll()

    @Query("SELECT * FROM ProgramacionDet WHERE programacionDetId=:id")
    fun getProgramacionByIdTask(id: Int): ProgramacionDet
}