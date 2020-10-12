package com.itfperu.appitf.data.local.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.itfperu.appitf.data.local.model.*
import com.itfperu.appitf.helper.Mensaje
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.RequestBody

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
    fun getSync(u: Int): Observable<Sync>
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
    fun deletePerfil(m: Perfil): Completable

    //Moneda
    fun clearMoneda(): Completable
    fun syncMoneda(): Observable<List<Moneda>>
    fun insertMonedas(t: List<Moneda>): Completable
    fun getMonedas(): LiveData<List<Moneda>>
    fun verificateMoneda(c: Moneda): Completable
    fun sendMoneda(c: Moneda): Observable<Mensaje>
    fun insertMoneda(c: Moneda, m: Mensaje): Completable
    fun getMonedaById(id: Int): LiveData<Moneda>

    //CATEGORIA
    fun clearCategoria(): Completable
    fun syncCategoria(): Observable<List<Categoria>>
    fun insertCategorias(p: List<Categoria>): Completable
    fun getCategorias(): LiveData<List<Categoria>>
    fun verificateCategoria(c: Categoria): Completable
    fun sendCategoria(c: Categoria): Observable<Mensaje>
    fun insertCategoria(c: Categoria, m: Mensaje): Completable
    fun getCategoriaById(id: Int): LiveData<Categoria>


    // ESPECIALIDAD
    fun clearEspecialidad(): Completable
    fun syncEspecialidad(): Observable<List<Especialidad>>
    fun insertEspecialidads(p: List<Especialidad>): Completable
    fun getEspecialidads(): LiveData<List<Especialidad>>
    fun verificateEspecialidad(c: Especialidad): Completable
    fun sendEspecialidad(c: Especialidad): Observable<Mensaje>
    fun insertEspecialidad(c: Especialidad, m: Mensaje): Completable
    fun getEspecialidadById(id: Int): LiveData<Especialidad>

    // PRODUCTO
    fun clearProducto(): Completable
    fun syncProducto(): Observable<List<Producto>>
    fun insertProductos(p: List<Producto>): Completable
    fun getProductos(): LiveData<List<Producto>>
    fun verificateProducto(c: Producto): Completable
    fun sendProducto(c: Producto): Observable<Mensaje>
    fun insertProducto(c: Producto, m: Mensaje): Completable
    fun getProductoById(id: Int): LiveData<Producto>

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

    // VISITA
    fun clearVisita(): Completable
    fun syncVisita(): Observable<List<Visita>>
    fun insertVisitas(p: List<Visita>): Completable
    fun getVisitas(): LiveData<List<Visita>>
    fun verificateVisita(c: Visita): Completable
    fun sendVisita(c: Visita): Observable<Mensaje>
    fun insertVisita(c: Visita, m: Mensaje): Completable
    fun getVisitaById(id: Int): LiveData<Visita>

    //FERIADO
    fun clearFeriado(): Completable
    fun syncFeriado(): Observable<List<Feriado>>
    fun insertFeriados(p: List<Feriado>): Completable
    fun getFeriados(): LiveData<List<Feriado>>
    fun verificateFeriado(c: Feriado): Completable
    fun sendFeriado(c: Feriado): Observable<Mensaje>
    fun insertFeriado(c: Feriado, m: Mensaje): Completable
    fun getFeriadoById(id: Int): LiveData<Feriado>

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

    //CATEGORIA
    fun clearCiclo(): Completable
    fun syncCiclo(): Observable<List<Ciclo>>
    fun insertCiclos(p: List<Ciclo>): Completable
    fun getCiclos(): LiveData<List<Ciclo>>
    fun verificateCiclo(c: Ciclo): Completable
    fun sendCiclo(c: Ciclo): Observable<Mensaje>
    fun insertCiclo(c: Ciclo, m: Mensaje): Completable
    fun getCicloById(id: Int): LiveData<Ciclo>

    //Actividad
    fun clearActividad(): Completable
    fun syncActividad(u: Int, c: Int, e: Int, t: Int): Observable<List<Actividad>>
    fun insertActividads(p: List<Actividad>): Completable
    fun getActividads(tipo:Int): LiveData<List<Actividad>>
    fun sendActividad(body: RequestBody): Observable<Mensaje>
    fun insertActividad(c: Actividad): Completable
    fun getActividadById(id: Int): LiveData<Actividad>
    fun getEstados(tipo: String): LiveData<List<Estado>>
    fun getCicloProceso(): LiveData<List<Ciclo>>
    fun getDuracion(): LiveData<List<Duracion>>
    fun getActividadTask(tipo:Int): Observable<List<Actividad>>
    fun updateEnabledActividad(t: Mensaje): Completable
    fun getNombreUsuario(): LiveData<String>

    //Medico
    fun clearSolMedico(): Completable
    fun syncSolMedico(
        u: Int, fi: String, ff: String, e: Int, t: Int
    ): Observable<List<SolMedico>>

    fun insertSolMedicos(p: List<SolMedico>): Completable
    fun getSolMedicos(tipoMedico:Int): LiveData<List<SolMedico>>
    fun verificateSolMedico(c: SolMedico): Completable
    fun sendSolMedico(body: RequestBody): Observable<Mensaje>
    fun insertSolMedico(c: SolMedico, m: Mensaje): Completable
    fun getSolMedicoById(id: Int): LiveData<SolMedico>
    fun getSolMedicoTask(tipoMedico:Int): Observable<List<SolMedico>>
    fun updateEnabledMedico(t: Mensaje): Completable

    fun getMedicoById(id: Int): LiveData<Medico>
    fun insertMedico(m: Medico): Completable
    fun getIdentificadores(): LiveData<List<Identificador>>

    fun getDepartamentos(): LiveData<List<Departamento>>
    fun getProvincias(): LiveData<List<Provincia>>
    fun getDistritos(): LiveData<List<Distrito>>
    fun getMedicos(): LiveData<List<Medico>>

    fun getDireccionesById(id: Int): LiveData<List<MedicoDireccion>>
    fun insertDireccion(m: MedicoDireccion): Completable
    fun getMedicosById(solMedicoId: Int): LiveData<List<Medico>>
    fun getMedicoId(): LiveData<Int>
    fun getSolMedicoId(): LiveData<Int>
    fun deleteMedico(m: Medico): Completable
    fun insertSolMedicoCab(c: SolMedico): Completable
    fun deleteDireccion(m: MedicoDireccion): Completable
    fun getDireccionById(id: Int): LiveData<MedicoDireccion>

    fun syncTarget(u: Int, c: Int, e: Int, n: Int): Observable<List<TargetM>>
    fun insertTargets(p: List<TargetM>): Completable
    fun getTargets(): LiveData<List<TargetM>>

    fun sendTarget(body: RequestBody): Observable<Mensaje>
    fun insertTarget(c: TargetCab, m: Mensaje): Completable
    fun getTargetById(id: Int): LiveData<TargetM>

    fun getTargetsAltas(t: String,tipo:Int): LiveData<List<TargetCab>>
    fun syncTargetCab(
        u: Int, fi: String, ff: String, e: Int, tt: String, t: Int
    ): Observable<List<TargetCab>>

    fun insertTargetsCab(p: List<TargetCab>): Completable
    fun getTargetDetById(targetId: Int): LiveData<List<TargetDet>>
    fun getTargetDetId(): LiveData<Int>
    fun getTargetCabId(): LiveData<Int>

    fun insertTargetDet(d: TargetDet): Completable
    fun insertTargetCab(c: TargetCab): Completable
    fun getTargetCabTask(tipoTarget: String): Observable<List<TargetCab>>
    fun updateEnabledTargetCab(t: Mensaje): Completable

    fun getCheckMedicos(): LiveData<PagedList<Medico>>
    fun getCheckMedicos(s: String): LiveData<PagedList<Medico>>
    fun saveMedico(cabId: Int, tipoTarget: String): Completable
    fun updateCheckMedico(s: Medico): Completable

}