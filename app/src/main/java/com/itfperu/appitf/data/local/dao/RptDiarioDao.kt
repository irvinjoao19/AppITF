package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.RptDiario

@Dao
interface RptDiarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRptDiarioTask(c: RptDiario)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRptDiarioListTask(c: List<RptDiario>)

    @Update
    fun updateRptDiarioTask(vararg c: RptDiario)

    @Delete
    fun deleteRptDiarioTask(c: RptDiario)

    @Query("SELECT * FROM RptDiario")
    fun getRptDiario(): LiveData<List<RptDiario>>

    @Query("DELETE FROM RptDiario")
    fun deleteAll()

    @Query("SELECT id FROM RptDiario ORDER BY id DESC LIMIT 1")
    fun getMaxIdRpt(): Int

    @Query("SELECT * FROM RptDiario")
    fun getRptDiarioCabecera(): LiveData<RptDiario>
}