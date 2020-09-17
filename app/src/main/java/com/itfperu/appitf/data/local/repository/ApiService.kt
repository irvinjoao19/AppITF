package com.itfperu.appitf.data.local.repository

import com.itfperu.appitf.data.local.model.*
import com.itfperu.appitf.helper.Mensaje
import io.reactivex.Observable
import okhttp3.RequestBody
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
    fun getFeriados(): Observable<List<Feriado>>

    @Headers("Cache-Control: no-cache")
    @GET("Categorias")
    fun getCategorias(): Observable<List<Categoria>>

    @Headers("Cache-Control: no-cache")
    @GET("Productos")
    fun getProductos(): Observable<List<Producto>>

    @Headers("Cache-Control: no-cache")
    @GET("TipoProductos")
    fun getTipoProductos(): Observable<List<TipoProducto>>

    @Headers("Cache-Control: no-cache")
    @GET("Visitas")
    fun getVisitas(): Observable<List<Visita>>

    @Headers("Cache-Control: no-cache")
    @POST("SaveCategoria")
    fun saveCategoria(@Body body: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @GET("Especialidades")
    fun getEspecialidades(): Observable<List<Especialidad>>

    @Headers("Cache-Control: no-cache")
    @POST("SaveEspecialidades")
    fun saveEspecialidades(@Body body: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("SavePerfil")
    fun savePerfil(@Body body: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("SaveMoneda")
    fun saveMoneda(@Body body: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("SaveProducto")
    fun saveProducto(@Body body: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("SaveTipoProducto")
    fun saveTipoProducto(@Body body: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("SaveVisita")
    fun saveVisita(@Body body: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("SaveFeriado")
    fun saveFeriado(@Body body: RequestBody): Observable<Mensaje>
}