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

    //usuario
    fun getUsuarioIdTask(): Int
    fun getUsuarioById(id: Int): LiveData<Usuario>
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
    fun updateUsuario(u: Usuario): Completable
    fun getUsuarioTask(): Observable<Usuario>
    fun sendUsuario(body: RequestBody): Observable<Mensaje>

    //Perfiles
    fun clearPerfil(): Completable
    fun syncPerfil(): Observable<List<Perfil>>
    fun insertPerfils(p: List<Perfil>): Completable
    fun getPerfils(): LiveData<List<Perfil>>
    fun getPerfilsActive(): LiveData<List<Perfil>>
    fun verificatePerfil(c: Perfil): Completable
    fun sendPerfil(c: Perfil): Observable<Mensaje>
    fun insertPerfil(c: Perfil, m: Mensaje): Completable
    fun getPerfilById(id: Int): LiveData<Perfil>
    fun removePerfil(i:Int): Observable<Mensaje>
    fun deletePerfil(m:Perfil): Completable

    //Moneda
    fun clearMoneda(): Completable
    fun syncMoneda(): Observable<List<Moneda>>
    fun insertMonedas(t: List<Moneda>): Completable
    fun getMonedas(): LiveData<List<Moneda>>
    fun verificateMoneda(c: Moneda): Completable
    fun sendMoneda(c: Moneda): Observable<Mensaje>
    fun insertMoneda(c: Moneda, m: Mensaje): Completable
    fun getMonedaById(id: Int): LiveData<Moneda>
    fun removeMoneda(i:Int): Observable<Mensaje>
    fun deleteMoneda(m:Moneda): Completable

    //CATEGORIA
    fun clearCategoria(): Completable
    fun syncCategoria(): Observable<List<Categoria>>
    fun insertCategorias(p: List<Categoria>): Completable
    fun getCategorias(): LiveData<List<Categoria>>
    fun verificateCategoria(c: Categoria): Completable
    fun sendCategoria(c: Categoria): Observable<Mensaje>
    fun insertCategoria(c: Categoria, m: Mensaje): Completable
    fun getCategoriaById(id: Int): LiveData<Categoria>
    fun removeCategoria(i:Int): Observable<Mensaje>
    fun deleteCategoria(c:Categoria): Completable

    // ESPECIALIDAD
    fun clearEspecialidad(): Completable
    fun syncEspecialidad(): Observable<List<Especialidad>>
    fun insertEspecialidads(p: List<Especialidad>): Completable
    fun getEspecialidads(): LiveData<List<Especialidad>>
    fun verificateEspecialidad(c: Especialidad): Completable
    fun sendEspecialidad(c: Especialidad): Observable<Mensaje>
    fun insertEspecialidad(c: Especialidad, m: Mensaje): Completable
    fun getEspecialidadById(id: Int): LiveData<Especialidad>
    fun removeEspecialidad(i:Int): Observable<Mensaje>
    fun deleteEspecialidad(e:Especialidad): Completable

    // PRODUCTO
    fun clearProducto(): Completable
    fun syncProducto(): Observable<List<Producto>>
    fun insertProductos(p: List<Producto>): Completable
    fun getProductos(): LiveData<List<Producto>>
    fun verificateProducto(c: Producto): Completable
    fun sendProducto(c: Producto): Observable<Mensaje>
    fun insertProducto(c: Producto, m: Mensaje): Completable
    fun getProductoById(id: Int): LiveData<Producto>
    fun removeProducto(i:Int): Observable<Mensaje>
    fun deleteProducto(p:Producto): Completable

    // TIPO PRODUCTO
    fun clearTipoProducto(): Completable
    fun syncTipoProducto(): Observable<List<TipoProducto>>
    fun insertTipoProductos(p: List<TipoProducto>): Completable
    fun getTipoProductos(): LiveData<List<TipoProducto>>
    fun getTipoProductoActive(): LiveData<List<TipoProducto>>
    fun verificateTipoProducto(c: TipoProducto): Completable
    fun sendTipoProducto(c: TipoProducto): Observable<Mensaje>
    fun insertTipoProducto(c: TipoProducto, m: Mensaje): Completable
    fun getTipoProductoById(id: Int): LiveData<TipoProducto>
    fun removeTipoProducto(i:Int): Observable<Mensaje>
    fun deleteTipoProducto(t:TipoProducto): Completable

    // VISITA
    fun clearVisita(): Completable
    fun syncVisita(): Observable<List<Visita>>
    fun insertVisitas(p: List<Visita>): Completable
    fun getVisitas(): LiveData<List<Visita>>
    fun verificateVisita(c: Visita): Completable
    fun sendVisita(c: Visita): Observable<Mensaje>
    fun insertVisita(c: Visita, m: Mensaje): Completable
    fun getVisitaById(id: Int): LiveData<Visita>
    fun removeVisita(i:Int): Observable<Mensaje>
    fun deleteVisita(v: Visita): Completable

    //FERIADO
    fun clearFeriado(): Completable
    fun syncFeriado(): Observable<List<Feriado>>
    fun insertFeriados(p: List<Feriado>): Completable
    fun getFeriados(): LiveData<List<Feriado>>
    fun verificateFeriado(c: Feriado): Completable
    fun sendFeriado(c: Feriado): Observable<Mensaje>
    fun insertFeriado(c: Feriado, m: Mensaje): Completable
    fun getFeriadoById(id: Int): LiveData<Feriado>
    fun removeFeriado(i:Int): Observable<Mensaje>
    fun deleteFeriado(f:Feriado): Completable

    //CONTROL
    fun syncControl(): Observable<List<Control>>
    fun insertControls(t: List<Control>): Completable
    fun getControls(): LiveData<List<Control>>

    //Personal
    fun clearPersonal(): Completable
    fun syncPersonal(): Observable<List<Personal>>
    fun insertPersonals(p: List<Personal>): Completable
    fun getPersonals(): LiveData<List<Personal>>
    fun verificatePersonal(c: Personal): Completable
    fun sendPersonal(c: Personal): Observable<Mensaje>
    fun insertPersonal(c: Personal, m: Mensaje): Completable
    fun getPersonalById(id: Int): LiveData<Personal>
    fun getSupervisores(): LiveData<List<Personal>>
    fun removePersonal(i:Int): Observable<Mensaje>
    fun deletePersonal(p:Personal): Completable

    //CATEGORIA
    fun clearCiclo(): Completable
    fun syncCiclo(): Observable<List<Ciclo>>
    fun insertCiclos(p: List<Ciclo>): Completable
    fun getCiclos(): LiveData<List<Ciclo>>
    fun verificateCiclo(c:Ciclo) : Completable
    fun sendCiclo(c: Ciclo): Observable<Mensaje>
    fun insertCiclo(c: Ciclo, m: Mensaje): Completable
    fun getCicloById(id: Int): LiveData<Ciclo>
}