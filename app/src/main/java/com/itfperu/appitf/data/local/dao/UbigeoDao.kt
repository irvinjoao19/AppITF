package com.itfperu.appitf.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itfperu.appitf.data.local.model.Ubigeo

@Dao
interface UbigeoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUbigeoTask(c: Ubigeo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUbigeoListTask(c: List<Ubigeo>)

    @Update
    fun updateUbigeoTask(vararg c: Ubigeo)

    @Delete
    fun deleteUbigeoTask(c: Ubigeo)

    @Query("SELECT * FROM Ubigeo")
    fun getUbigeos(): LiveData<List<Ubigeo>>

    @Query("DELETE FROM Ubigeo")
    fun deleteAll()

    @Query("SELECT * FROM Ubigeo GROUP BY nombreDepartamento")
    fun getDepartamentos(): LiveData<List<Ubigeo>>

    @Query("SELECT * FROM Ubigeo WHERE codDepartamento =:c GROUP BY provincia")
    fun getProvincias(c: String): LiveData<List<Ubigeo>>

    @Query("SELECT * FROM Ubigeo WHERE codDepartamento=:c AND codProvincia =:c2")
    fun getDistritos(c: String, c2: String): LiveData<List<Ubigeo>>
}