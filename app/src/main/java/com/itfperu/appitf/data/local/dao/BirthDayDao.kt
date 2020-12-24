package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.BirthDay

@Dao
interface BirthDayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBirthDayTask(c: BirthDay)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBirthDayListTask(c: List<BirthDay>)

    @Update
    fun updateBirthDayTask(vararg c: BirthDay)

    @Delete
    fun deleteBirthDayTask(c: BirthDay)

    @Query("SELECT * FROM BirthDay")
    fun getBirthDay(): LiveData<BirthDay>

    @Query("SELECT usuarioId FROM BirthDay")
    fun getBirthDayId(): Int

    @Query("DELETE FROM BirthDay")
    fun deleteAll()

    @Query("SELECT usuarioId FROM BirthDay")
    fun getBirthDayIdTask(): Int

    @Query("SELECT * FROM BirthDay")
    fun getBirthDays(): LiveData<List<BirthDay>>

    @Query("SELECT * FROM BirthDay WHERE mesId =:mes")
    fun getBirthDaysByMonth(mes: Int): LiveData<List<BirthDay>>
}