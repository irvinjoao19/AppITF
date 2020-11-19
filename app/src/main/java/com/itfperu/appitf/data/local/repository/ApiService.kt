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
    @GET("Sync")
    fun getSync(
        @Query("usuarioId") u: Int, // usuario
    ): Observable<Sync>

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
    @GET("Ciclos")
    fun getCiclos(): Observable<List<Ciclo>>

    @Headers("Cache-Control: no-cache")
    @GET("Actividades")
    fun getActividad(
        @Query("u") u: Int, // usuario
        @Query("c") c: Int, // ciclo
        @Query("e") e: Int,  // estado
        @Query("t") t: Int  // tipo
    ): Observable<List<Actividad>>

    @Headers("Cache-Control: no-cache")
    @GET("Targets")
//    int u, int c, int e, int n
    fun getTargets(
        @Query("u") u: Int, // usuario
        @Query("c") c: Int, // categoria
        @Query("e") e: Int, // especialidad
        @Query("n") n: Int,  // nro contacto
    ): Observable<List<TargetM>>

    @Headers("Cache-Control: no-cache")
    @GET("TargetsCab")
    fun getTargetsCab(
        @Query("u") u: Int, // usuario
        @Query("fi") fi: String, // fecha inicial
        @Query("ff") ff: String, // fecha final
        @Query("e") e: Int, // estado
        @Query("tt") tt: String,  // tipo target
        @Query("t") t: Int,  // tipo
    ): Observable<List<TargetCab>>

    @Headers("Cache-Control: no-cache")
    @GET("Medicos")
    fun getMedico(
        @Query("u") u: Int, // usuario
        @Query("fi") fi: String, // desde
        @Query("ff") ff: String, // hasta
        @Query("e") e: Int,  // estado
        @Query("t") t: Int  // tipo
    ): Observable<List<SolMedico>>

    @Headers("Cache-Control: no-cache")
    @GET("Programacion")
    fun getProgramacion(@Query("u") u: Int, @Query("c") c: Int): Observable<List<Programacion>>

    @Headers("Cache-Control: no-cache")
    @GET("NuevaDireccion")
    fun getNuevaDireccion(
        @Query("u") u: Int, // usuario
        @Query("fi") fi: String, // desde
        @Query("ff") ff: String, // hasta
        @Query("e") e: Int,  // estado
        @Query("t") t: Int  // tipo
    ): Observable<List<NuevaDireccion>>


    @Headers("Cache-Control: no-cache")
    @GET("ProgramacionPerfil")
    fun getProgramacionPerfil(
        @Query("m") m: Int, // medicoId
    ): Observable<List<ProgramacionPerfil>>

    @Headers("Cache-Control: no-cache")
    @GET("ProgramacionPerfilDetalle")
    fun getProgramacionPerfilDetalle(
        @Query("m") m: Int, // medicoId
        @Query("p") p: String, // nombre producto
    ): Observable<List<ProgramacionPerfilDetalle>>

    @Headers("Cache-Control: no-cache")
    @GET("ProgramacionReja")
    fun getProgramacionReja(
        @Query("e") m: Int, // especialidadId
    ): Observable<List<ProgramacionReja>>

    @Headers("Cache-Control: no-cache")
    @GET("PuntoContacto")
    fun getPuntoContacto(
        @Query("u") u: Int,
        @Query("fi") fi: String,
        @Query("ff") ff: String
    ): Observable<List<PuntoContacto>>

    @Headers("Cache-Control: no-cache")
    @GET("AlertaActividad")
    fun getAlertaActividad(
        @Query("c") c: Int,
        @Query("m") m: Int,
        @Query("u") u: Int
    ): Observable<Mensaje>

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

    @Headers("Cache-Control: no-cache")
    @POST("SaveCiclo")
    fun saveCiclo(@Body body: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("SaveActividad")
    fun saveActividad(@Body body: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("SaveMedico")
    fun saveMedico(@Body body: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("SaveTarget")
    fun saveTarget(@Body body: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("SaveProgramacion")
    fun sendProgramacion(@Body body: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("SaveNuevaDireccion")
    fun sendNuevaDireccion(@Body body: RequestBody): Observable<Mensaje>

    @Headers("Cache-Control: no-cache")
    @POST("SavePuntoContacto")
    fun sendPuntoContacto(@Body body: RequestBody): Observable<Mensaje>
}