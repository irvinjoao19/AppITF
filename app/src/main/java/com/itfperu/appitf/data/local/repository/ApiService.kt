package com.itfperu.appitf.data.local.repository

import com.itfperu.appitf.data.local.model.*
import com.itfperu.appitf.helper.Mensaje
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Headers("Cache-Control: no-cache")
    @POST("Login")
    fun getLogin(@Body body: RequestBody): Observable<Usuario>

    @Headers("Cache-Control: no-cache")
    @POST("Logout")
    fun getLogout(@Body body: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("Sync")
    fun getSync(@Body body: RequestBody): Observable<Sync>

    @Headers("Cache-Control: no-cache")
    @GET("Perfiles")
    fun getPerfiles(): Observable<List<Perfil>>

    @Headers("Cache-Control: no-cache")
    @GET("Monedas")
    fun getMonedas(): Observable<List<Moneda>>

    @Headers("Cache-Control: no-cache")
    @GET("Feriados")
    fun syncFeriados(): Observable<List<Feriado>>
}