package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.Control

@Dao
interface ControlDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertControlTask(c: Control)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertControlListTask(c: List<Control>)

    @Update
    fun updateControlTask(vararg c: Control)

    @Delete
    fun deleteControlTask(c: Control)

    @Query("SELECT * FROM Control")
    fun getControl(): LiveData<Control>

    @Query("SELECT * FROM Control")
    fun getControls(): LiveData<List<Control>>

    @Query("DELETE FROM Control")
    fun deleteAll()
}