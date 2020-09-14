package com.itfperu.appitf.data.local.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.itfperu.appitf.data.local.model.*
import com.itfperu.appitf.helper.Mensaje
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Call

interface AppRepository {

    fun getUsuarioIdTask(): Int

    fun getUsuario(): LiveData<Usuario>

    fun getUsuarioService(
        usuario: String, password: String, imei: String, version: String
    ): Observable<Usuario>

    fun getLogout(login: String): Observable<Mensaje>

    fun insertUsuario(u: Usuario): Completable

    fun deleteSesion(): Completable

    fun deleteSync(): Completable

    fun getSync(u: Int, e: Int, p: Int): Observable<Sync>

    fun saveSync(s: Sync): Completable

    fun syncPerfiles(): Observable<List<Perfil>>

    fun insertPerfiles(p: List<Perfil>) : Completable

    fun getPerfiles(): LiveData<List<Perfil>>

    fun syncMonedas():  Observable<List<Moneda>>

    fun insertMonedas(t: List<Moneda>) : Completable

    fun syncFeriados(): Observable<List<Feriado>>

    fun insertFeriados(t: List<Feriado>): Completable
}