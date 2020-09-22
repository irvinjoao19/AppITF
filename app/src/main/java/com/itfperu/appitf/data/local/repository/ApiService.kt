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
    @POST("UpdateUsuario")
    fun sendUsuario(@Body body: RequestBody): Observable<Mensaje>

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
    @GET("Personals")
    fun getPersonals(): Observable<List<Personal>>

    @Headers("Cache-Control: no-cache")
    @GET("TipoProductos")
    fun getTipoProductos(): Observable<List<TipoProducto>>

    @Headers("Cache-Control: no-cache")
    @GET("Visitas")
    fun getVisitas(): Observable<List<Visita>>

    @Headers("Cache-Control: no-cache")
    @GET("Control")
    fun getControl(): Observable<List<Control>>

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
    @POST("SaveProductos")
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

    @Headers("Cache-Control: no-cache")
    @POST("SaveUsuario")
    fun savePersonal(@Body body: RequestBody): Observable<Mensaje>

    // remove - delete

    @Headers("Cache-Control: no-cache")
    @POST("RemovePerfil")
    fun removePerfil(@Query("id") i: Int): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("RemoveMoneda")
    fun removeMoneda(@Query("id") i: Int): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("RemoveFeriado")
    fun removeFeriado(@Query("id") i: Int): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("RemoveCategoria")
    fun removeCategoria(@Query("id") i: Int): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("RemoveEspecialidades")
    fun removeEspecialidades(@Query("id") i: Int): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("RemoveProductos")
    fun removeProductos(@Query("id") i: Int): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("RemoveVisita")
    fun removeVisita(@Query("id") i: Int): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("RemoveTipoProducto")
    fun removeTipoProducto(@Query("id") i: Int): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("RemoveUsuario")
    fun removeUsuario(@Query("id") i: Int): Observable<Mensaje>
    
}