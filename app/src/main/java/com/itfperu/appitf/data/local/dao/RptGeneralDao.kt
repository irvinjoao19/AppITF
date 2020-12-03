package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.RptGeneral

@Dao
interface RptGeneralDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRptGeneralTask(c: RptGeneral)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRptGeneralListTask(c: List<RptGeneral>)

    @Update
    fun updateRptGeneralTask(vararg c: RptGeneral)

    @Delete
    fun deleteRptGeneralTask(c: RptGeneral)

    @Query("SELECT * FROM RptGeneral")
    fun getRptGeneralCabecera(): LiveData<RptGeneral>

    @Query("SELECT * FROM RptGeneral")
    fun getRptGeneral(): LiveData<List<RptGeneral>>

    @Query("DELETE FROM RptGeneral")
    fun deleteAll()

    @Query("SELECT id FROM RptGeneral ORDER BY id DESC LIMIT 1")
    fun getMaxIdRpt(): Int
}