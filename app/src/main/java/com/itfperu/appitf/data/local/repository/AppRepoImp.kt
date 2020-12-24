package com.itfperu.appitf.data.local.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.itfperu.appitf.data.local.AppDataBase
import com.itfperu.appitf.data.local.model.*
import com.itfperu.appitf.helper.Mensaje
import com.google.gson.Gson
import com.itfperu.appitf.helper.MensajeDetalle
import com.itfperu.appitf.helper.Util
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.RequestBody

class AppRepoImp(private val apiService: ApiService, private val dataBase: AppDataBase) :
    AppRepository {

    override fun getUsuarioIdTask(): Int {
        return dataBase.usuarioDao().getUsuarioIdTask()
    }

    override fun getUsuarioById(id: Int): LiveData<Usuario> {
        return dataBase.usuarioDao().getUsuarioById(id)
    }

    override fun getUsuario(): LiveData<Usuario> {
        return dataBase.usuarioDao().getUsuario()
    }

    override fun getUsuarioService(
        usuario: String, password: String, imei: String, version: String
    ): Observable<Usuario> {
        val u = Filtro(usuario, password, imei, version)
        val json = Gson().toJson(u)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.getLogin(body)
    }

    override fun getAccesos(usuarioId: Int): LiveData<List<Accesos>> {
        return dataBase.accesosDao().getAccesosById(usuarioId)
    }

    override fun getLogout(login: String): Observable<Mensaje> {
        val u = Filtro(login, "", "", "")
        val json = Gson().toJson(u)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.getLogout(body)
    }

    override fun insertUsuario(u: Usuario): Completable {
        return Completable.fromAction {
            dataBase.usuarioDao().insertUsuarioTask(u)
            val a: List<Accesos>? = u.accesos
            if (a != null) {
                dataBase.accesosDao().insertAccesosListTask(a)
            }
        }
    }

    override fun deleteSesion(): Completable {
        return Completable.fromAction {
            dataBase.usuarioDao().deleteAll()
            dataBase.accesosDao().deleteAll()
            dataBase.perfilDao().deleteAll()
            dataBase.monedaDao().deleteAll()
            dataBase.feriadoDao().deleteAll()
            dataBase.categoriaDao().deleteAll()
            dataBase.especialidadDao().deleteAll()
            dataBase.productoDao().deleteAll()
            dataBase.tipoProductoDao().deleteAll()
            dataBase.visitaDao().deleteAll()
            dataBase.controlDao().deleteAll()
            dataBase.personalDao().deleteAll()
            dataBase.cicloDao().deleteAll()
            dataBase.actividadDao().deleteAll()
            dataBase.estadoDao().deleteAll()
            dataBase.duracionDao().deleteAll()
            dataBase.solMedicoDao().deleteAll()
            dataBase.medicoDao().deleteAll()
            dataBase.medicoDireccionDao().deleteAll()
            dataBase.identificadorDao().deleteAll()
            dataBase.targetDao().deleteAll()
            dataBase.targetCabDao().deleteAll()
            dataBase.targetDetDao().deleteAll()
            dataBase.targetInfoDao().deleteAll()
            dataBase.ubigeoDao().deleteAll()
            dataBase.programacionDao().deleteAll()
            dataBase.programacionDetDao().deleteAll()
            dataBase.stockDao().deleteAll()
            dataBase.nuevaDireccionDao().deleteAll()
            dataBase.puntoContactoDao().deleteAll()
            dataBase.rptGeneralDao().deleteAll()
            dataBase.rptDiarioDao().deleteAll()
        }
    }

    override fun deleteSync(): Completable {
        return Completable.fromAction {
            dataBase.perfilDao().deleteAll()
            dataBase.monedaDao().deleteAll()
            dataBase.feriadoDao().deleteAll()
            dataBase.categoriaDao().deleteAll()
            dataBase.especialidadDao().deleteAll()
            dataBase.productoDao().deleteAll()
            dataBase.tipoProductoDao().deleteAll()
            dataBase.visitaDao().deleteAll()
            dataBase.controlDao().deleteAll()
            dataBase.personalDao().deleteAll()
            dataBase.cicloDao().deleteAll()
            dataBase.actividadDao().deleteAll()
            dataBase.estadoDao().deleteAll()
            dataBase.duracionDao().deleteAll()
            dataBase.solMedicoDao().deleteAll()
            dataBase.medicoDao().deleteAll()
            dataBase.medicoDireccionDao().deleteAll()
            dataBase.identificadorDao().deleteAll()
            dataBase.targetDao().deleteAll()
            dataBase.targetCabDao().deleteAll()
            dataBase.targetDetDao().deleteAll()
            dataBase.targetInfoDao().deleteAll()
            dataBase.ubigeoDao().deleteAll()
            dataBase.programacionDao().deleteAll()
            dataBase.programacionDetDao().deleteAll()
            dataBase.stockDao().deleteAll()
            dataBase.nuevaDireccionDao().deleteAll()
            dataBase.puntoContactoDao().deleteAll()
            dataBase.rptGeneralDao().deleteAll()
            dataBase.rptDiarioDao().deleteAll()
        }
    }

    override fun getSync(u: Int): Observable<Sync> {
//        val f = Filtro("", "", "", "")
//        val json = Gson().toJson(f)
//        Log.i("TAG", json)
//        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.getSync(u)
    }

    override fun saveSync(s: Sync): Completable {
        return Completable.fromAction {
            val c: List<Ciclo>? = s.ciclos
            if (c != null) {
                dataBase.cicloDao().insertCicloListTask(c)
            }
            val e: List<Estado>? = s.estados
            if (e != null) {
                dataBase.estadoDao().insertEstadoListTask(e)
            }
            val d: List<Duracion>? = s.duracions
            if (d != null) {
                dataBase.duracionDao().insertDuracionListTask(d)
            }
            val p: List<Personal>? = s.personals
            if (p != null) {
                dataBase.personalDao().insertPersonalListTask(p)
            }
            val ca: List<Categoria>? = s.categorias
            if (ca != null) {
                dataBase.categoriaDao().insertCategoriaListTask(ca)
            }
            val id: List<Identificador>? = s.identificadors
            if (id != null) {
                dataBase.identificadorDao().insertIdentificadorListTask(id)
            }
            val es: List<Especialidad>? = s.especialidads
            if (es != null) {
                dataBase.especialidadDao().insertEspecialidadListTask(es)
            }
            val dp: List<Ubigeo>? = s.ubigeos
            if (dp != null) {
                dataBase.ubigeoDao().insertUbigeoListTask(dp)
            }
            val me: List<Medico>? = s.medicos
            if (me != null) {
                dataBase.medicoDao().insertMedicoListTask(me)
                for (m: Medico in me) {
                    val di: List<MedicoDireccion>? = m.direcciones
                    if (di != null) {
                        dataBase.medicoDireccionDao().insertMedicoDireccionListTask(di)
                    }
                }
            }
            val v1: List<Visita>? = s.visitas
            if (v1 != null) {
                dataBase.visitaDao().insertVisitaListTask(v1)
            }
        }
    }

    override fun updateUsuario(u: Usuario): Completable {
        return Completable.fromAction {
            dataBase.usuarioDao().updateUsuarioTask(u)
        }
    }

    override fun getUsuarioTask(): Observable<Usuario> {
        return Observable.create { e ->
            val u = dataBase.usuarioDao().getUsuarioTask()
            e.onNext(u)
            e.onComplete()
        }
    }

    override fun sendUsuario(body: RequestBody): Observable<Mensaje> {
        return apiService.sendUsuario(body)
    }

    override fun clearPerfil(): Completable {
        return Completable.fromAction {
            dataBase.perfilDao().deleteAll()
        }
    }

    override fun syncPerfil(): Observable<List<Perfil>> {
        return apiService.getPerfiles()
    }

    override fun insertPerfils(p: List<Perfil>): Completable {
        return Completable.fromAction {
            dataBase.perfilDao().insertPerfilListTask(p)
        }
    }

    override fun getPerfils(): LiveData<List<Perfil>> {
        return dataBase.perfilDao().getPerfiles()
    }

    override fun getPerfilsActive(): LiveData<List<Perfil>> {
        return dataBase.perfilDao().getPerfilsActive()
    }

    override fun verificatePerfil(c: Perfil): Completable {
        return Completable.fromAction {
            if (c.perfilId == 0) {
                val m: Perfil? =
                    dataBase.perfilDao().getPerfilByCod(c.codigo)
                if (m != null) {
                    error("El codigo de Rol existe... Ingrese otro")
                }
            }
        }
    }

    override fun sendPerfil(c: Perfil): Observable<Mensaje> {
        val json = Gson().toJson(c)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.savePerfil(body)
    }

    override fun insertPerfil(c: Perfil, m: Mensaje): Completable {
        return Completable.fromAction {
            if (m.codigoBase == 0) {
                c.perfilId = m.codigoRetorno
                dataBase.perfilDao().insertPerfilTask(c)
            } else {
                dataBase.perfilDao().updatePerfilTask(c)
            }
        }
    }

    override fun getPerfilById(id: Int): LiveData<Perfil> {
        return dataBase.perfilDao().getPerfilById(id)
    }


    override fun deletePerfil(m: Perfil): Completable {
        return Completable.fromAction {
            m.estado = "INACTIVO"
            m.estadoId = 0
            dataBase.perfilDao().updatePerfilTask(m)
        }
    }

    override fun clearMoneda(): Completable {
        return Completable.fromAction {
            dataBase.monedaDao().deleteAll()
        }
    }

    override fun syncMoneda(): Observable<List<Moneda>> {
        return apiService.getMonedas()
    }

    override fun insertMonedas(t: List<Moneda>): Completable {
        return Completable.fromAction {
            dataBase.monedaDao().insertMonedaListTask(t)
        }
    }

    override fun getMonedas(): LiveData<List<Moneda>> {
        return dataBase.monedaDao().getMonedaes()
    }

    override fun verificateMoneda(c: Moneda): Completable {
        return Completable.fromAction {
            if (c.monedaId == 0) {
                val m: Moneda? =
                    dataBase.monedaDao().getMonedaByCod(c.codigo)
                if (m != null) {
                    error("El codigo de moneda existe... Ingrese otro")
                }
            }
        }
    }

    override fun sendMoneda(c: Moneda): Observable<Mensaje> {
        val json = Gson().toJson(c)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.saveMoneda(body)
    }

    override fun insertMoneda(c: Moneda, m: Mensaje): Completable {
        return Completable.fromAction {
            if (m.codigoBase == 0) {
                c.monedaId = m.codigoRetorno
                dataBase.monedaDao().insertMonedaTask(c)
            } else {
                dataBase.monedaDao().updateMonedaTask(c)
            }
        }
    }

    override fun getMonedaById(id: Int): LiveData<Moneda> {
        return dataBase.monedaDao().getMonedaById(id)
    }

    override fun clearCategoria(): Completable {
        return Completable.fromAction {
            dataBase.categoriaDao().deleteAll()
        }
    }

    override fun syncCategoria(): Observable<List<Categoria>> {
        return apiService.getCategorias()
    }

    override fun insertCategorias(p: List<Categoria>): Completable {
        return Completable.fromAction {
            dataBase.categoriaDao().insertCategoriaListTask(p)
        }
    }

    override fun getCategorias(): LiveData<List<Categoria>> {
        return dataBase.categoriaDao().getCategorias()
    }

    override fun verificateCategoria(c: Categoria): Completable {
        return Completable.fromAction {
            if (c.categoriaId == 0) {
                val m: Categoria? =
                    dataBase.categoriaDao().getCategoriaByCod(c.codigo)
                if (m != null) {
                    error("El codigo de categoria existe... Ingrese otro")
                }
            }
        }
    }

    override fun sendCategoria(c: Categoria): Observable<Mensaje> {
        val json = Gson().toJson(c)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.saveCategoria(body)
    }

    override fun insertCategoria(c: Categoria, m: Mensaje): Completable {
        return Completable.fromAction {
            if (m.codigoBase == 0) {
                c.categoriaId = m.codigoRetorno
                dataBase.categoriaDao().insertCategoriaTask(c)
            } else {
                dataBase.categoriaDao().updateCategoriaTask(c)
            }
        }
    }

    override fun getCategoriaById(id: Int): LiveData<Categoria> {
        return dataBase.categoriaDao().getCategoriaById(id)
    }

    override fun clearEspecialidad(): Completable {
        return Completable.fromAction {
            dataBase.especialidadDao().deleteAll()
        }
    }

    override fun syncEspecialidad(): Observable<List<Especialidad>> {
        return apiService.getEspecialidades()
    }

    override fun insertEspecialidads(p: List<Especialidad>): Completable {
        return Completable.fromAction {
            dataBase.especialidadDao().insertEspecialidadListTask(p)
        }
    }

    override fun getEspecialidads(): LiveData<List<Especialidad>> {
        return dataBase.especialidadDao().getEspecialidades()
    }

    override fun verificateEspecialidad(c: Especialidad): Completable {
        return Completable.fromAction {
            if (c.especialidadId == 0) {
                val m: Especialidad? =
                    dataBase.especialidadDao().getEspecialidadByCod(c.codigo)
                if (m != null) {
                    error("El codigo de especialidad existe... Ingrese otro")
                }
            }
        }
    }

    override fun sendEspecialidad(c: Especialidad): Observable<Mensaje> {
        val json = Gson().toJson(c)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.saveEspecialidades(body)
    }

    override fun insertEspecialidad(c: Especialidad, m: Mensaje): Completable {
        return Completable.fromAction {
            if (m.codigoBase == 0) {
                c.especialidadId = m.codigoRetorno
                dataBase.especialidadDao().insertEspecialidadTask(c)
            } else {
                dataBase.especialidadDao().updateEspecialidadTask(c)
            }
        }
    }

    override fun getEspecialidadById(id: Int): LiveData<Especialidad> {
        return dataBase.especialidadDao().getEspecialidadById(id)
    }

    override fun clearProducto(): Completable {
        return Completable.fromAction {
            dataBase.productoDao().deleteAll()
        }
    }

    override fun syncProducto(): Observable<List<Producto>> {
        return apiService.getProductos()
    }

    override fun insertProductos(p: List<Producto>): Completable {
        return Completable.fromAction {
            dataBase.productoDao().insertProductoListTask(p)
        }
    }

    override fun getProductos(): LiveData<List<Producto>> {
        return dataBase.productoDao().getProductos()
    }

    override fun verificateProducto(c: Producto): Completable {
        return Completable.fromAction {
            if (c.productoId == 0) {
                val m: Producto? =
                    dataBase.productoDao().getProductoByCod(c.codigo)
                if (m != null) {
                    error("El codigo de Producto existe... Ingrese otro")
                }
            }
        }
    }

    override fun sendProducto(c: Producto): Observable<Mensaje> {
        val json = Gson().toJson(c)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.saveProducto(body)
    }

    override fun insertProducto(c: Producto, m: Mensaje): Completable {
        return Completable.fromAction {
            if (m.codigoBase == 0) {
                c.productoId = m.codigoRetorno
                dataBase.productoDao().insertProductoTask(c)
            } else {
                dataBase.productoDao().updateProductoTask(c)
            }
        }
    }

    override fun getProductoById(id: Int): LiveData<Producto> {
        return dataBase.productoDao().getProductoById(id)
    }

    override fun clearTipoProducto(): Completable {
        return Completable.fromAction {
            dataBase.tipoProductoDao().deleteAll()
        }
    }

    override fun syncTipoProducto(): Observable<List<TipoProducto>> {
        return apiService.getTipoProductos()
    }

    override fun insertTipoProductos(p: List<TipoProducto>): Completable {
        return Completable.fromAction {
            dataBase.tipoProductoDao().insertTipoProductoListTask(p)
        }
    }

    override fun getTipoProductos(): LiveData<List<TipoProducto>> {
        return dataBase.tipoProductoDao().getTipoProductos()
    }

    override fun getTipoProductoActive(): LiveData<List<TipoProducto>> {
        return dataBase.tipoProductoDao().getTipoProductoActive()
    }

    override fun verificateTipoProducto(c: TipoProducto): Completable {
        return Completable.fromAction {
            if (c.tipoProductoId == 0) {
                val m: TipoProducto? =
                    dataBase.tipoProductoDao().getTipoProductoByCod(c.codigo)
                if (m != null) {
                    error("El codigo de Tipo Producto existe... Ingrese otro")
                }
            }
        }
    }

    override fun sendTipoProducto(c: TipoProducto): Observable<Mensaje> {
        val json = Gson().toJson(c)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.saveTipoProducto(body)
    }

    override fun insertTipoProducto(c: TipoProducto, m: Mensaje): Completable {
        return Completable.fromAction {
            if (m.codigoBase == 0) {
                c.tipoProductoId = m.codigoRetorno
                dataBase.tipoProductoDao().insertTipoProductoTask(c)
            } else {
                dataBase.tipoProductoDao().updateTipoProductoTask(c)
            }
        }
    }

    override fun getTipoProductoById(id: Int): LiveData<TipoProducto> {
        return dataBase.tipoProductoDao().getTipoProductoById(id)
    }

    override fun clearVisita(): Completable {
        return Completable.fromAction {
            dataBase.visitaDao().deleteAll()
        }
    }

    override fun syncVisita(): Observable<List<Visita>> {
        return apiService.getVisitas()
    }

    override fun insertVisitas(p: List<Visita>): Completable {
        return Completable.fromAction {
            dataBase.visitaDao().insertVisitaListTask(p)
        }
    }

    override fun getVisitas(): LiveData<List<Visita>> {
        return dataBase.visitaDao().getVisitas()
    }

    override fun verificateVisita(c: Visita): Completable {
        return Completable.fromAction {
//            if (c. == "1"){
//                Throwable("Error de codigo")
//            }
        }
    }

    override fun sendVisita(c: Visita): Observable<Mensaje> {
        val json = Gson().toJson(c)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.saveVisita(body)
    }

    override fun insertVisita(c: Visita, m: Mensaje): Completable {
        return Completable.fromAction {
            if (m.codigoBase == 0) {
                c.visitaId = m.codigoRetorno
                dataBase.visitaDao().insertVisitaTask(c)
            } else {
                dataBase.visitaDao().updateVisitaTask(c)
            }
        }
    }

    override fun getVisitaById(id: Int): LiveData<Visita> {
        return dataBase.visitaDao().getVisitaById(id)
    }

    override fun clearFeriado(): Completable {
        return Completable.fromAction {
            dataBase.feriadoDao().deleteAll()
        }
    }

    override fun syncFeriado(): Observable<List<Feriado>> {
        return apiService.getFeriados()
    }

    override fun insertFeriados(p: List<Feriado>): Completable {
        return Completable.fromAction {
            dataBase.feriadoDao().insertFeriadoListTask(p)
        }
    }

    override fun getFeriados(): LiveData<List<Feriado>> {
        return dataBase.feriadoDao().getFeriados()
    }

    override fun verificateFeriado(c: Feriado): Completable {
        return Completable.fromAction {
            if (c.feriadoId == 0) {
                val m: Feriado? = dataBase.feriadoDao().getPersonalByCod(c.fecha)
                if (m != null) {
                    error("La fecha de feriado existe... Ingrese otro")
                }
            }
        }
    }

    override fun sendFeriado(c: Feriado): Observable<Mensaje> {
        val json = Gson().toJson(c)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.saveFeriado(body)
    }

    override fun insertFeriado(c: Feriado, m: Mensaje): Completable {
        return Completable.fromAction {
            if (m.codigoBase == 0) {
                c.feriadoId = m.codigoRetorno
                dataBase.feriadoDao().insertFeriadoTask(c)
            } else {
                dataBase.feriadoDao().updateFeriadoTask(c)
            }
        }
    }

    override fun getFeriadoById(id: Int): LiveData<Feriado> {
        return dataBase.feriadoDao().getFeriadoById(id)
    }

    override fun syncControl(): Observable<List<Control>> {
        return apiService.getControl()
    }

    override fun insertControls(t: List<Control>): Completable {
        return Completable.fromAction {
            dataBase.controlDao().insertControlListTask(t)
        }
    }

    override fun getControls(): LiveData<List<Control>> {
        return dataBase.controlDao().getControls()
    }

    override fun clearPersonal(): Completable {
        return Completable.fromAction {
            dataBase.personalDao().deleteAll()
        }
    }

    override fun syncPersonal(): Observable<List<Personal>> {
        return apiService.getPersonals()
    }

    override fun insertPersonals(p: List<Personal>): Completable {
        return Completable.fromAction {
            dataBase.personalDao().insertPersonalListTask(p)
        }
    }

    override fun getPersonals(): LiveData<List<Personal>> {
        return dataBase.personalDao().getPersonals()
    }

    override fun verificatePersonal(c: Personal): Completable {
        return Completable.fromAction {
            if (c.personalId == 0) {
                val m: Personal? =
                    dataBase.personalDao().getPersonalByCod(c.login)
                if (m != null) {
                    error("El login de usuario existe... Ingrese otro")
                }
            }
        }
    }

    override fun sendPersonal(c: Personal): Observable<Mensaje> {
        val json = Gson().toJson(c)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.savePersonal(body)
    }

    override fun insertPersonal(c: Personal, m: Mensaje): Completable {
        return Completable.fromAction {
            if (m.codigoBase == 0) {
                c.personalId = m.codigoRetorno
                dataBase.personalDao().insertPersonalTask(c)
            } else {
                dataBase.personalDao().updatePersonalTask(c)
            }
        }
    }

    override fun getPersonalById(id: Int): LiveData<Personal> {
        return dataBase.personalDao().getPersonalById(id)
    }

    override fun getSupervisores(): LiveData<List<Personal>> {
        return dataBase.personalDao().getSupervisores()
    }

    override fun clearCiclo(): Completable {
        return Completable.fromAction {
            dataBase.cicloDao().deleteAll()
        }
    }

    override fun syncCiclo(): Observable<List<Ciclo>> {
        return apiService.getCiclos()
    }

    override fun insertCiclos(p: List<Ciclo>): Completable {
        return Completable.fromAction {
            dataBase.cicloDao().insertCicloListTask(p)
        }
    }

    override fun getCiclos(): LiveData<List<Ciclo>> {
        return dataBase.cicloDao().getCiclos()
    }

    override fun verificateCiclo(c: Ciclo): Completable {
        return Completable.fromAction {
            if (c.estado == 4) {
                val m: Ciclo? = dataBase.cicloDao().getCicloProceso(4)
                if (m != null) {
                    error("No puede haber varios ciclos en PROCESO")
                }
            }
        }
    }

    override fun sendCiclo(c: Ciclo): Observable<Mensaje> {
        val json = Gson().toJson(c)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.saveCiclo(body)
    }

    override fun insertCiclo(c: Ciclo, m: Mensaje): Completable {
        return Completable.fromAction {
            if (m.codigoBase == 0) {
                c.cicloId = m.codigoRetorno
                dataBase.cicloDao().insertCicloTask(c)
            } else {
                dataBase.cicloDao().updateCicloTask(c)
            }
        }
    }

    override fun getCicloById(id: Int): LiveData<Ciclo> {
        return dataBase.cicloDao().getCicloById(id)
    }

    override fun clearActividad(): Completable {
        return Completable.fromAction {
            dataBase.actividadDao().deleteAll()
        }
    }

    override fun syncActividad(
        u: Int, c: Int, e: Int, t: Int, ul: Int
    ): Observable<List<Actividad>> {
        return apiService.getActividad(u, c, e, t, ul)
    }

    override fun insertActividads(p: List<Actividad>): Completable {
        return Completable.fromAction {
            for (a: Actividad in p) {
                val det: Actividad? =
                    dataBase.actividadDao().getActividadOffLineByIdTask(a.identity)
                if (det == null) {
                    dataBase.actividadDao().insertActividadTask(a)
                } else {
                    if (det.active == 0) {
                        dataBase.actividadDao().updateActividadTask(a)
                    }
                }
            }
        }
    }

    override fun sendActividad(body: RequestBody): Observable<Mensaje> {
        return apiService.saveActividad(body)
    }

    override fun insertActividad(c: Actividad, t: Mensaje): Completable {
        return Completable.fromAction {
            c.identity = t.codigoRetorno
            c.usuario = dataBase.usuarioDao().getUsuarioNombre()
            val a: Actividad? = dataBase.actividadDao().getActividadByIdTask(c.actividadId)
            if (a == null) {
                dataBase.actividadDao().insertActividadTask(c)
            } else {
                dataBase.actividadDao().updateActividadTask(c)
            }
        }
    }

    override fun getActividadById(id: Int): LiveData<Actividad> {
        return dataBase.actividadDao().getActividadById(id)
    }

    override fun getEstados(tipo: String): LiveData<List<Estado>> {
        return dataBase.estadoDao().getEstados(tipo)
    }

    override fun getCicloProceso(): LiveData<List<Ciclo>> {
        return dataBase.cicloDao().getCicloProceso()
    }

    override fun getFirstCicloProceso(): LiveData<Ciclo> {
        return dataBase.cicloDao().getFirstCicloProceso()
    }

    override fun getDuracion(): LiveData<List<Duracion>> {
        return dataBase.duracionDao().getDuracions()
    }

    override fun getActividadTask(tipo: Int): Observable<List<Actividad>> {
        return Observable.create { e ->
            val data: List<Actividad> = dataBase.actividadDao().getActividadTask(tipo)
            if (data.isEmpty()) {
                e.onError(Throwable("No hay datos por enviar"))
                e.onComplete()
                return@create
            }
            e.onNext(data)
            e.onComplete()
        }
    }

    override fun updateEnabledActividad(t: Mensaje): Completable {
        return Completable.fromAction {
            dataBase.actividadDao().updateEnabledActividad(t.codigoBase, t.codigoRetorno)
        }
    }

    override fun getNombreUsuario(): LiveData<String> {
        return dataBase.usuarioDao().getNombreUsuario()
    }

    override fun getActividades(u: Int, c: Int, e: Int, t: Int): LiveData<List<Actividad>> {
        return if (t == 1) {
            if (c == 0) {
                if (e == 0) {
                    dataBase.actividadDao().getActividadesU(u, t)
                } else {
                    dataBase.actividadDao().getActividadesU(u, e, t)
                }
            } else {
                if (e == 0) {
                    dataBase.actividadDao().getActividadesCiclo(c, t)
                } else {
                    dataBase.actividadDao().getActividades(c, e, t)
                }
            }
        } else {
            if (c != 0) {
                if (e != 0) {
                    dataBase.actividadDao().getActividades(c, e, t)
                } else {
                    dataBase.actividadDao().getActividadesCiclo(c, t)
                }
            } else {
                if (e == 0) {
                    dataBase.actividadDao().getActividades(t)
                } else {
                    dataBase.actividadDao().getActividades(e, t)
                }
            }
        }
    }

    override fun getAlertaActividad(
        cicloId: Int, medicoId: Int, usuarioId: Int
    ): Observable<Mensaje> {
        return apiService.getAlertaActividad(cicloId, medicoId, usuarioId)
    }

    override fun clearSolMedico(): Completable {
        return Completable.fromAction {
        }
    }

    override fun syncSolMedico(
        u: Int, fi: String, ff: String, e: Int, t: Int
    ): Observable<List<SolMedico>> {
        return apiService.getMedico(u, fi, ff, e, t)
    }

    override fun insertSolMedicos(p: List<SolMedico>): Completable {
        return Completable.fromAction {
            for (a: SolMedico in p) {
                val cab: SolMedico? =
                    dataBase.solMedicoDao().getSolMedicoOffLineIdTask(a.solMedicoId)
                if (cab == null) {
                    dataBase.solMedicoDao().insertMedicoTask(a)
                } else {
                    if (cab.estado == 0) {
                        dataBase.solMedicoDao().updateMedicoTask(a)
                    }
                }
                val b: List<Medico>? = a.medicos
                if (b != null) {
                    for (c: Medico in b) {
                        val det: Medico? =
                            dataBase.medicoDao().getMedicoOffLineByIdTask(c.identity)
                        if (det == null) {
                            dataBase.medicoDao().insertMedicoTask(c)
                        } else {
                            dataBase.medicoDao().updateMedicoTask(c)
                        }

                        val dir: List<MedicoDireccion>? = c.direcciones
                        if (dir != null) {
                            for (d: MedicoDireccion in dir) {
                                val md: MedicoDireccion? = dataBase.medicoDireccionDao()
                                    .getMedicoDireccionOffLineByIdTask(d.identity)
                                if (md == null) {
                                    dataBase.medicoDireccionDao().insertMedicoDireccionTask(d)
                                } else {
                                    dataBase.medicoDireccionDao().updateMedicoDireccionTask(d)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun getSolMedicos(
        u: Int, fi: String, ff: String, e: Int, t: Int
    ): LiveData<List<SolMedico>> {
        return if (t == 1) {
            if (e == 0) {
                dataBase.solMedicoDao().getMedicos(u, fi, ff, t)
            } else {
                dataBase.solMedicoDao().getMedicos(u, fi, ff, e, t)
            }
        } else {
            if (e == 0) {
                dataBase.solMedicoDao().getMedicos(fi, ff, t)
            } else {
                dataBase.solMedicoDao().getMedicos(fi, ff, e, t)
            }
        }
    }

    override fun verificateSolMedico(c: SolMedico): Completable {
        return Completable.fromAction {

        }
    }

    override fun sendSolMedico(body: RequestBody): Observable<Mensaje> {
        return apiService.saveMedico(body)
    }

    override fun insertSolMedico(c: SolMedico, m: Mensaje): Completable {
        return Completable.fromAction {
            if (m.codigoBase == 0) {
                c.solMedicoId = m.codigoRetorno
                dataBase.solMedicoDao().insertMedicoTask(c)
            } else {
                dataBase.solMedicoDao().updateMedicoTask(c)
            }
        }
    }

    override fun getSolMedicoById(id: Int): LiveData<SolMedico> {
        return dataBase.solMedicoDao().getMedicoById(id)
    }

    override fun getSolMedicoTask(tipoMedico: Int): Observable<List<SolMedico>> {
        return Observable.create { e ->
            val a: ArrayList<SolMedico> = ArrayList()
            val data: List<SolMedico> = dataBase.solMedicoDao().getSolMedicoTask(tipoMedico)
            if (data.isEmpty()) {
                e.onError(Throwable("No hay datos por enviar"))
                e.onComplete()
                return@create
            }
            for (r: SolMedico in data) {
                val d: ArrayList<Medico> = ArrayList()
                val detalle = dataBase.medicoDao()
                    .getMedicoBySolIdTask(r.solMedicoId)
                for (m: Medico in detalle) {
//                    m.direcciones = dataBase.medicoDireccionDao().getDireccionIdTask(m.medicoId)
                    d.add(m)
                }
                r.medicos = d
                a.add(r)
            }
            e.onNext(a)
            e.onComplete()
        }
    }

    override fun updateEnabledMedico(t: Mensaje): Completable {
        return Completable.fromAction {
            dataBase.solMedicoDao().updateEnabledMedico(t.codigoBase, t.codigoRetorno)

//            val d: List<MensajeDetalle>? = t.detalle
//            if (d != null) {
//                for (m: MensajeDetalle in d) {
//                    dataBase.medicoDao().updateEnabledMedico(m.detalleBaseId, m.detalleRetornoId)
//                    val e = m.subDetalles
//                    if (e != null) {
//                        for (dt: MensajeDetalleDet in e) {
//                            dataBase.medicoDireccionDao()
//                                .updateEnabledDireccion(dt.codigoBase, dt.codigoRetorno)
//                        }
//                    }
//                }
//            }
        }
    }

    override fun getMedicoById(id: Int): LiveData<Medico> {
        return dataBase.medicoDao().getMedicoById(id)
    }

    override fun insertMedico(m: Medico, t: Mensaje): Completable {
        return Completable.fromAction {
            m.identity = t.codigoRetorno
            val e: Medico? = dataBase.medicoDao().getMedicoByIdTask(m.medicoId)
            if (e == null) {
                dataBase.medicoDao().insertMedicoTask(m)
            } else {
                dataBase.medicoDao().updateMedicoTask(m)
            }

            val ta: TargetM? = dataBase.targetDao().getTargetByMedico(m.medicoId)
            if (ta != null) {
                ta.nombreUsuario =
                    String.format("%s %s %s", m.apellidoP, m.apellidoM, m.nombreMedico)
                ta.descripcionCategoria = m.nombreCategoria
                ta.descripcionEspecialidad = m.nombreEspecialidad
                dataBase.targetDao().updateTargetTask(ta)
            }


//            val e: Medico? = dataBase.medicoDao().getMedicoByIdTask(m.medicoId)
//            if (e == null) {
//                val a: Medico? =
//                    dataBase.medicoDao().getMedicoCmpIdentificador(m.cpmMedico, m.identificadorId)
//                if (a != null) {
//                    error("Ya existe un medico con el mismo CMP e Identificador")
//                } else {
//                    dataBase.medicoDao().insertMedicoTask(m)
//                }
//            } else {
//                val a = dataBase.medicoDireccionDao().getMedicoDirecciones(e.medicoId)
//                if (a > 0) {
//                    m.active = 1
//                }
//                dataBase.medicoDao().updateMedicoTask(m)
//            }
        }
    }

    override fun getIdentificadores(): LiveData<List<Identificador>> {
        return dataBase.identificadorDao().getIdentificadores()
    }

    override fun getDepartamentos(): LiveData<List<Ubigeo>> {
        return dataBase.ubigeoDao().getDepartamentos()
    }

    override fun getProvincias(cod: String): LiveData<List<Ubigeo>> {
        return dataBase.ubigeoDao().getProvincias(cod)
    }

    override fun getDistritos(cod: String, cod2: String): LiveData<List<Ubigeo>> {
        return dataBase.ubigeoDao().getDistritos(cod, cod2)
    }

    override fun getMedicos(): LiveData<List<Medico>> {
        return dataBase.medicoDao().getMedicos()
    }

    override fun getMedicosByEstado(e: Int): LiveData<List<Medico>> {
        return dataBase.medicoDao().getMedicosByEstado(e)
    }

    override fun getMedicosByEstado(e: Int, u: Int): LiveData<List<Medico>> {
        return dataBase.medicoDao().getMedicosByEstado(e, u)
    }

    override fun getDireccionesById(id: Int): LiveData<List<MedicoDireccion>> {
        return dataBase.medicoDireccionDao().getMedicoDireccionesById(id)
    }

    override fun insertDireccion(m: MedicoDireccion, t: Mensaje): Completable {
        return Completable.fromAction {
            dataBase.medicoDao().closeMedico(m.medicoId)
            m.identityDetalle = t.codigoRetorno
            if (m.medicoDireccionId == 0)
                dataBase.medicoDireccionDao().insertMedicoDireccionTask(m)
            else
                dataBase.medicoDireccionDao().updateMedicoDireccionTask(m)
        }
    }

    override fun getMedicosById(solMedicoId: Int): LiveData<List<Medico>> {
        return dataBase.medicoDao().getMedicosById(solMedicoId)
    }

    override fun getMedicoId(): LiveData<Int> {
        return dataBase.medicoDao().getMaxIdMedico()
    }

    override fun getSolMedicoId(): LiveData<Int> {
        return dataBase.solMedicoDao().getMaxIdSolMedico()
    }

    override fun deleteMedico(m: Medico): Completable {
        return Completable.fromAction {
            dataBase.medicoDireccionDao().deleteDireccionesById(m.medicoId)
            dataBase.medicoDao().deleteMedicoTask(m)
        }
    }

    override fun insertSolMedicoCabInit(c: SolMedico): Completable {
        return Completable.fromAction {
            c.usuarioId = dataBase.usuarioDao().getUsuarioId()
            val a: SolMedico? = dataBase.solMedicoDao().getSolMedicoByIdTask(c.solMedicoId)
            if (a == null) {
                c.estado = 3
                c.descripcionEstado = "Completar Formulario"
                c.fechaInicio = Util.getFirstDay()
                c.fechaFinal = Util.getLastaDay()
                dataBase.solMedicoDao().insertMedicoTask(c)
            } else {

                dataBase.solMedicoDao().updateMedicoTask(c)
            }
        }
    }

    override fun insertSolMedicoCab(c: SolMedico): Completable {
        return Completable.fromAction {
            val list = dataBase.medicoDao().getMedicosInactivos(c.solMedicoId)
            if (list > 0) {
                error("Cada Medico debe de tener una direccion minimo")
            } else {
//                c.usuario = dataBase.usuarioDao().getUsuarioNombre()
                c.usuarioId = dataBase.usuarioDao().getUsuarioId()
                val a: SolMedico? = dataBase.solMedicoDao().getSolMedicoByIdTask(c.solMedicoId)
                if (a == null) {
                    c.fechaInicio = Util.getFirstDay()
                    c.fechaFinal = Util.getLastaDay()
                    dataBase.solMedicoDao().insertMedicoTask(c)
                } else {
                    dataBase.solMedicoDao().updateMedicoTask(c)
                }
            }
        }
    }

    override fun deleteDireccion(m: MedicoDireccion): Completable {
        return Completable.fromAction {
            dataBase.medicoDireccionDao().deleteMedicoDireccionTask(m)
        }
    }

    override fun getDireccionById(id: Int): LiveData<MedicoDireccion> {
        return dataBase.medicoDireccionDao().getDireccionById(id)
    }

    override fun deleteSolMedico(m: SolMedico): Completable {
        return Completable.fromAction {
            val l: List<Medico>? = dataBase.medicoDao().getMedicoBySolIdTask(m.solMedicoId)
            if (l != null) {
                for (m1: Medico in l) {
                    dataBase.medicoDireccionDao().deleteDireccionesById(m1.medicoId)
                }
                dataBase.medicoDao().deleteMedicoById(m.solMedicoId)
            }
            dataBase.solMedicoDao().deleteMedicoTask(m)
        }
    }

    override fun clearTarget(): Completable {
        return Completable.fromAction {
            dataBase.targetDao().deleteAll()
        }
    }

    override fun syncTarget(u: Int, c: Int, e: Int, n: Int, s: String): Observable<List<TargetM>> {
        return apiService.getTargets(u, c, e, n, s)
    }

    override fun insertTargets(p: List<TargetM>): Completable {
        return Completable.fromAction {
            dataBase.targetDao().insertTargetListTask(p)
            for (a: TargetM in p) {
                val b: List<Medico>? = a.medicos
                if (b != null) {
                    for (c: Medico in b) {
                        val det: Medico? =
                            dataBase.medicoDao().getMedicoOffLineByIdTask(c.identity)
                        if (det == null) {
                            dataBase.medicoDao().insertMedicoTask(c)
                        } else {
                            dataBase.medicoDao().updateMedicoTask(c)
                        }

                        val dir: List<MedicoDireccion>? = c.direcciones
                        if (dir != null) {
                            for (d: MedicoDireccion in dir) {
                                val md: MedicoDireccion? = dataBase.medicoDireccionDao()
                                    .getMedicoDireccionOffLineByIdTask(d.identity)
                                if (md == null) {
                                    dataBase.medicoDireccionDao().insertMedicoDireccionTask(d)
                                } else {
                                    dataBase.medicoDireccionDao().updateMedicoDireccionTask(d)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun getTargets(): LiveData<List<TargetM>> {
        return dataBase.targetDao().getTargets()
    }


    override fun sendTarget(body: RequestBody): Observable<Mensaje> {
        return apiService.saveTarget(body)
    }

    override fun insertTarget(c: TargetCab, m: Mensaje): Completable {
        return Completable.fromAction {
            if (m.codigoBase == 0) {
                c.targetCabId = m.codigoRetorno
                dataBase.targetCabDao().insertTargetCabTask(c)
            } else {
                dataBase.targetCabDao().updateTargetCabTask(c)
            }
        }
    }

    override fun getTargetById(id: Int): LiveData<TargetM> {
        return dataBase.targetDao().getTargetById(id)
    }

    override fun getTargetsAltas(t: String, tipo: Int): LiveData<List<TargetCab>> {
        return dataBase.targetCabDao().getTargetsAltas(t, tipo)
    }

    override fun getTargetsAltas(
        u: Int, fi: String, ff: String, e: Int, t: Int, tt: String
    ): LiveData<List<TargetCab>> {
        return if (t == 1) {
            if (e == 0) {
                dataBase.targetCabDao().getTargetsAltas(u, fi, ff, t, tt)
            } else {
                dataBase.targetCabDao().getTargetsAltas(u, fi, ff, e, t, tt)
            }
        } else {
            if (e == 0) {
                dataBase.targetCabDao().getTargetsAltas(fi, ff, t, tt)
            } else {
                dataBase.targetCabDao().getTargetsAltas(fi, ff, e, t, tt)
            }
        }
    }

    override fun syncTargetCab(
        u: Int, fi: String, ff: String, e: Int, tt: String, t: Int, ul: Int
    ): Observable<List<TargetCab>> {
        return apiService.getTargetsCab(u, fi, ff, e, tt, t, ul)
    }

    override fun insertTargetsCab(p: List<TargetCab>): Completable {
        return Completable.fromAction {
            for (a: TargetCab in p) {
                val cab: TargetCab? =
                    dataBase.targetCabDao().getTargetCabOffLineIdTask(a.identity)
                if (cab == null) {
                    dataBase.targetCabDao().insertTargetCabTask(a)
                    val b: List<TargetDet>? = a.detalle
                    if (b != null) {
                        for (d: TargetDet in b) {
                            val det: TargetDet? =
                                dataBase.targetDetDao().getTargetDetOffLineByIdTask(d.identity)
                            if (det == null) {
                                dataBase.targetDetDao().insertTargetDetTask(d)
                            } else {
                                dataBase.targetDetDao().updateTargetDetTask(d)
                            }

                            val i: List<TargetInfo>? = d.infos
                            if (i != null) {
                                dataBase.targetInfoDao().insertTargetListTask(i)
                            }
                        }
                    }
                } else {
                    if (cab.active == 0) {
                        dataBase.targetCabDao().updateTargetCabTask(a)
                    }
                }
            }
        }
    }

    override fun getTargetDetById(targetId: Int): LiveData<List<TargetDet>> {
        return dataBase.targetDetDao().getTargetDetById(targetId)
    }

    override fun getTargetDetId(): LiveData<Int> {
        return dataBase.targetDetDao().getMaxIdTargetDet()
    }

    override fun getTargetCabId(): LiveData<Int> {
        return dataBase.targetCabDao().getMaxIdTargetCab()
    }

    override fun getTarget(targetId: Int): LiveData<TargetCab> {
        return dataBase.targetCabDao().getTargetById(targetId)
    }

    override fun getTargetInfo(targetDetId: Int): LiveData<List<TargetInfo>> {
        return dataBase.targetInfoDao().getTargetInfo(targetDetId)
    }

    override fun updateEstadoTargetDet(t: TargetDet): Completable {
        return Completable.fromAction {
            dataBase.targetDetDao().updateTargetDetTask(t)
            val u = dataBase.usuarioDao().getUsuarioId()
            dataBase.targetCabDao().updateForSendTarget(t.targetCabId, u)
        }
    }

    override fun insertTargetDet(d: TargetDet): Completable {
        return Completable.fromAction {
            val a: TargetDet? = dataBase.targetDetDao().getTargetDetByIdTask(d.targetDetId)
            if (a == null) {
                dataBase.targetDetDao().insertTargetDetTask(d)
            } else {
                dataBase.targetDetDao().updateTargetDetTask(d)
            }
        }
    }

    override fun insertTargetCab(c: TargetCab): Completable {
        return Completable.fromAction {
            c.usuarioSolicitante = dataBase.usuarioDao().getUsuarioNombre()
            c.usuarioId = dataBase.usuarioDao().getUsuarioId()

            val a: TargetCab? = dataBase.targetCabDao().getTargetCabByIdTask(c.targetCabId)
            if (a == null) {
                c.fechaInicio = Util.getFirstDay()
                c.fechaFinal = Util.getLastaDay()
                dataBase.targetCabDao().insertTargetCabTask(c)
            } else {
                dataBase.targetCabDao().updateTargetCabTask(c)
            }
        }
    }

    override fun getTargetCabTask(tipoTarget: String, tipo: Int): Observable<List<TargetCab>> {
        return Observable.create { e ->
            val a: ArrayList<TargetCab> = ArrayList()
            val data: List<TargetCab> = dataBase.targetCabDao().getTargetCabTask(tipoTarget, tipo)
            if (data.isEmpty()) {
                e.onError(Throwable("No hay datos por enviar"))
                e.onComplete()
                return@create
            }
            for (r: TargetCab in data) {
                r.detalle = dataBase.targetDetDao().getTargetDetIdTask(r.targetCabId)
                a.add(r)
            }
            e.onNext(a)
            e.onComplete()
        }
    }

    override fun updateEnabledTargetCab(t: Mensaje): Completable {
        return Completable.fromAction {
            dataBase.targetCabDao().updateEnabledTargetCab(t.codigoBase, t.codigoRetorno)
            val det: List<MensajeDetalle>? = t.detalle
            if (det != null) {
                for (d: MensajeDetalle in det) {
                    dataBase.targetDetDao()
                        .updateEnabledTargetDet(d.detalleBaseId, d.detalleRetornoId)
                }
            }
        }
    }

    override fun deleteTargetDet(targetId: Int): Completable {
        return Completable.fromAction {
            dataBase.targetDetDao().deleteTargetDet(targetId)
        }
    }

    override fun getCheckMedicos(t: String, u: Int): LiveData<PagedList<Medico>> {
        return if (t == "B") {
            dataBase.medicoDao().getCheckMedicos(u).toLiveData(
                Config(pageSize = 20, enablePlaceholders = true)
            )
        } else {
            dataBase.medicoDao().getCheckMedicos().toLiveData(
                Config(pageSize = 20, enablePlaceholders = true)
            )
        }
    }

    override fun getCheckMedicos(t: String, u: Int, s: String): LiveData<PagedList<Medico>> {
        return if (t == "B") {
            dataBase.medicoDao().getCheckMedicos(s, u).toLiveData(
                Config(pageSize = 20, enablePlaceholders = true)
            )
        } else {
            dataBase.medicoDao().getCheckMedicos(s).toLiveData(
                Config(pageSize = 20, enablePlaceholders = true)
            )
        }
    }

    override fun saveMedico(cabId: Int, tipoTarget: String): Completable {
        return Completable.fromAction {
            val stock = dataBase.medicoDao().getMedicoSelected(true)
            for (s: Medico in stock) {
                val a = TargetDet()
                a.targetCabId = cabId
                a.medicoId = s.medicoId
                a.cmp = s.cpmMedico
                a.nombreCategoria = s.nombreCategoria
                a.nombreEspecialidad = s.nombreEspecialidad
                a.nombreMedico = String.format("%s %s %s", s.nombreMedico, s.apellidoP, s.apellidoM)
                a.nroContacto = 1
                a.tipoTarget = tipoTarget
                a.visitadoPor = s.visitadoPor
                a.estadoTarget = 16

                if (!dataBase.targetDetDao().getMedicoExits(a.medicoId, a.targetCabId)) {
                    dataBase.targetDetDao().insertTargetDetTask(a)
                }
            }
            dataBase.medicoDao().enabledMedicoSelected(false)
        }
    }

    override fun updateCheckMedico(s: Medico): Completable {
        return Completable.fromAction {
            dataBase.medicoDao().updateMedicoTask(s)
        }
    }

    override fun getSolMedicoCab(solMedicoId: Int): LiveData<SolMedico> {
        return dataBase.solMedicoDao().getSolMedicoCab(solMedicoId)
    }

    override fun syncProgramacion(u: Int, c: Int): Observable<List<Programacion>> {
        return apiService.getProgramacion(u, c)
    }

    override fun insertProgramacions(p: List<Programacion>): Completable {
        return Completable.fromAction {
            for (a: Programacion in p) {
                val cab: Programacion? =
                    dataBase.programacionDao().getProgramacionOffLineIdTask(a.identity)
                if (cab == null) {
                    dataBase.programacionDao().insertProgramacionTask(a)
                } else {
                    if (cab.active == 0) {
                        dataBase.programacionDao().updateProgramacionTask(a)
                    }
                }
                val b: List<ProgramacionDet>? = a.productos
                if (b != null) {
                    for (d: ProgramacionDet in b) {
                        val det: ProgramacionDet? =
                            dataBase.programacionDetDao()
                                .getProgramacionDetOffLineByIdTask(d.identity)
                        if (det == null) {
                            dataBase.programacionDetDao().insertProgramacionDetTask(d)
                        } else {
                            dataBase.programacionDetDao().updateProgramacionDetTask(d)
                        }
                    }
                }
            }
        }
    }

    override fun getProgramacionTask(): Observable<List<Programacion>> {
        return Observable.create { e ->
            val a: ArrayList<Programacion> = ArrayList()
            val data: List<Programacion> = dataBase.programacionDao().getProgramacionTask()
            if (data.isEmpty()) {
                e.onError(Throwable("No hay datos por enviar"))
                e.onComplete()
                return@create
            }
            for (r: Programacion in data) {
//                r.productos =
//                    dataBase.programacionDetDao().getProgramacionesByIdTask(r.programacionId)
                a.add(r)
            }
            e.onNext(a)
            e.onComplete()
        }
    }

    override fun sendProgramacion(body: RequestBody): Observable<Mensaje> {
        return apiService.sendProgramacion(body)
    }

    override fun sendProgramacionOnline(p: Programacion): Observable<Mensaje> {
        val json = Gson().toJson(p)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.sendProgramacion(body)
    }

    override fun updateEnabledProgramacion(t: Mensaje): Completable {
        return Completable.fromAction {
            dataBase.programacionDao().updateEnabledProgramacion(t.codigoBase, t.codigoRetorno)
            dataBase.programacionDetDao()
                .updateEnabledProgramacionDet(t.codigoBase)

//            val d: List<MensajeDetalle>? = t.detalle
//            if (d != null) {
//                for (m: MensajeDetalle in d) {
//                    dataBase.programacionDetDao()
//                        .updateEnabledProgramacionDet(m.detalleBaseId, m.detalleRetornoId)
//                }
//            }
        }
    }

    override fun getProgramaciones(): LiveData<List<Programacion>> {
        return dataBase.programacionDao().getProgramaciones()
    }

    override fun getProgramaciones(e: Int, s: String): LiveData<List<Programacion>> {
        return if (s.isEmpty()) {
            dataBase.programacionDao().getProgramaciones(e)
        } else {
            dataBase.programacionDao().getProgramaciones(e, String.format("%s%s%s", "%", s, "%"))
        }
    }

    override fun getProgramacionId(): LiveData<Int> {
        return dataBase.programacionDao().getMaxIdProgramacion()
    }

    override fun getProgramacionById(programacionId: Int): LiveData<Programacion> {
        return dataBase.programacionDao().getProgramacionById(programacionId)
    }

    override fun insertProgramacion(p: Programacion): Completable {
        return Completable.fromAction {
            val cab: Programacion? =
                dataBase.programacionDao().getProgramacionByIdTask(p.programacionId)
            if (cab == null) {
                dataBase.programacionDao().insertProgramacionTask(p)
            } else {
                dataBase.programacionDao().updateProgramacionTask(p)
            }
        }
    }

    override fun getProgramacionesDetById(programacionId: Int): LiveData<List<ProgramacionDet>> {
        return dataBase.programacionDetDao().getProgramacionesById(programacionId)
    }

    override fun getProgramacionDetById(id: Int): LiveData<ProgramacionDet> {
        return dataBase.programacionDetDao().getProgramacionById(id)
    }

    override fun getStocks(): LiveData<List<Stock>> {
        return dataBase.stockDao().getStocks()
    }

    override fun verificateProgramacionDet(p: ProgramacionDet): Completable {
        return Completable.fromAction {
            val validate: ProgramacionDet? =
                dataBase.programacionDetDao()
                    .getValidateProducto(p.programacionId, p.ordenProgramacion)
            if (validate != null) {
                if (validate.programacionDetId != p.programacionDetId) {
                    error("Ingrese otro numero de orden")
                }
            }
        }
    }

    override fun sendProgramacionDet(p: ProgramacionDet): Observable<Mensaje> {
        val json = Gson().toJson(p)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.sendProgramacionDet(body)
    }

    override fun insertProgramacionDet(p: ProgramacionDet, t: Mensaje): Completable {
        return Completable.fromAction {
            p.identity = t.codigoRetorno

            val cab: ProgramacionDet? =
                dataBase.programacionDetDao().getProgramacionByIdTask(p.programacionDetId)
            if (cab == null) {
                dataBase.programacionDetDao().insertProgramacionDetTask(p)
                return@fromAction
            }
            dataBase.programacionDetDao().updateProgramacionDetTask(p)
        }
    }

    override fun sendDeleteProgramacionDet(id: Int): Observable<Mensaje> {
        return apiService.sendDeleteProgramacionDet(id)
    }

    override fun deleteProgramacionDet(p: ProgramacionDet): Completable {
        return Completable.fromAction {
            dataBase.programacionDetDao().deleteProgramacionDetTask(p)
        }
    }

    override fun syncDireccion(
        u: Int, fi: String, ff: String, e: Int, t: Int
    ): Observable<List<NuevaDireccion>> {
        return apiService.getNuevaDireccion(u, fi, ff, e, t)
    }

    override fun insertDireccions(p: List<NuevaDireccion>): Completable {
        return Completable.fromAction {
            for (n: NuevaDireccion in p) {
                val t: NuevaDireccion? =
                    dataBase.nuevaDireccionDao().getNuevaDireccionOffLine(n.identity)
                if (t == null) {
                    dataBase.nuevaDireccionDao().insertNuevaDireccionTask(n)
                } else {
                    dataBase.nuevaDireccionDao().updateNuevaDireccionTask(n)
                }
            }
        }
    }

    override fun getDirecciones(): LiveData<List<NuevaDireccion>> {
        return dataBase.nuevaDireccionDao().getNuevaDirecciones()
    }

    override fun getDirecciones(
        fi: String, ff: String, e: Int, t: Int
    ): LiveData<List<NuevaDireccion>> {
        return if (e != 0)
            dataBase.nuevaDireccionDao().getNuevaDirecciones(fi, ff, e, t)
        else
            dataBase.nuevaDireccionDao().getNuevaDirecciones(fi, ff, t)
    }

    override fun getDireccionTask(tipo: Int): Observable<List<NuevaDireccion>> {
        return Observable.create { e ->
            val data: List<NuevaDireccion> =
                dataBase.nuevaDireccionDao().getNuevaDireccionTask(tipo)
            if (data.isEmpty()) {
                e.onError(Throwable("No hay datos por enviar"))
                e.onComplete()
                return@create
            }
            e.onNext(data)
            e.onComplete()
        }
    }

    override fun sendDireccion(body: RequestBody): Observable<Mensaje> {
        return apiService.sendNuevaDireccion(body)
    }

    override fun updateEnabledDireccion(t: Mensaje): Completable {
        return Completable.fromAction {
            dataBase.nuevaDireccionDao()
                .updateEnabledDireccion(t.codigoBase, t.codigoRetorno, t.codigoAlterno)
        }
    }

    override fun insertNuevaDireccion(p: NuevaDireccion): Completable {
        return Completable.fromAction {
            val n: NuevaDireccion? =
                dataBase.nuevaDireccionDao().getNuevaDireccionById(p.solDireccionId)
            if (n == null) {
                p.aprobador = dataBase.usuarioDao().getUsuarioNombre()
                p.fechaInicio = Util.getFirstDay()
                p.fechaFinal = Util.getLastaDay()
                dataBase.nuevaDireccionDao().insertNuevaDireccionTask(p)
            } else {
                dataBase.nuevaDireccionDao().updateNuevaDireccionTask(p)
            }
        }
    }

    override fun getNuevaDireccionMaxId(): LiveData<Int> {
        return dataBase.nuevaDireccionDao().getNuevaDireccionMaxId()
    }

    override fun getNuevaDireccionId(id: Int): LiveData<NuevaDireccion> {
        return dataBase.nuevaDireccionDao().getNuevaDireccionId(id)
    }

    override fun syncProgramacionPerfil(medicoId: Int): Observable<List<ProgramacionPerfil>> {
        return apiService.getProgramacionPerfil(medicoId)
    }

    override fun syncProgramacionReja(especialidadId: Int): Observable<List<ProgramacionReja>> {
        return apiService.getProgramacionReja(especialidadId)
    }

    override fun syncProgramacionPerfilDetalle(
        medicoId: Int, s: String
    ): Observable<List<ProgramacionPerfilDetalle>> {
        return apiService.getProgramacionPerfilDetalle(medicoId, s)
    }

    override fun syncPuntoContacto(
        u: Int, fi: String, ff: String
    ): Observable<List<PuntoContacto>> {
        return apiService.getPuntoContacto(u, fi, ff)
    }

    override fun insertPuntoContactos(p: List<PuntoContacto>): Completable {
        return Completable.fromAction {
            for (c: PuntoContacto in p) {
                val con: PuntoContacto? =
                    dataBase.puntoContactoDao().getPuntoContactoOffLine(c.puntoContactoId)
                if (con == null) {
                    dataBase.puntoContactoDao().insertPuntoContactoTask(c)
                } else {
                    if (con.active == 0) {
                        dataBase.puntoContactoDao().updatePuntoContactoTask(c)
                    }
                }
            }
        }
    }

    override fun getPuntoContactos(f: String): LiveData<List<PuntoContacto>> {
        return dataBase.puntoContactoDao().getPuntoContactos(f)
    }

    override fun getPuntoContactoTask(): Observable<List<PuntoContacto>> {
        return Observable.create { e ->
            val data: List<PuntoContacto> =
                dataBase.puntoContactoDao().getPuntoContactoTask()
            if (data.isEmpty()) {
                e.onError(Throwable("No hay datos por enviar"))
                e.onComplete()
                return@create
            }
            e.onNext(data)
            e.onComplete()
        }
    }

    override fun sendPuntoContacto(body: RequestBody): Observable<Mensaje> {
        return apiService.sendPuntoContacto(body)
    }


    override fun sendOnlinePuntoContacto(p: PuntoContacto): Observable<Mensaje> {
        val json = Gson().toJson(p)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.sendPuntoContacto(body)
    }

    override fun updateEnabledPuntoContacto(t: Mensaje): Completable {
        return Completable.fromAction {
            dataBase.puntoContactoDao().updateEnabledPuntoContacto(t.codigoBase)
        }
    }

    override fun insertPuntoContacto(p: PuntoContacto): Completable {
        return Completable.fromAction {
            dataBase.puntoContactoDao().updatePuntoContactoTask(p)
        }
    }

    override fun synsProductos(u: Int): Observable<List<Stock>> {
        return apiService.getStocks(u)
    }

    override fun insertProductoStocks(s: List<Stock>): Completable {
        return Completable.fromAction {
            dataBase.stockDao().insertStockListTask(s)
        }
    }

    override fun clearStockMantenimiento(): Completable {
        return Completable.fromAction {
            dataBase.stockMantenimientoDao().deleteAll()
        }
    }

    override fun syncStockMantenimiento(u: Int, c: Int): Observable<List<StockMantenimiento>> {
        return apiService.getStockMantenimiento(u, c)
    }

    override fun insertStockMantenimientos(p: List<StockMantenimiento>): Completable {
        return Completable.fromAction {
            dataBase.stockMantenimientoDao().insertStockMantenimientoListTask(p)
        }
    }

    override fun getStockMantenimientos(): LiveData<List<StockMantenimiento>> {
        return dataBase.stockMantenimientoDao().getStockMantenimiento()
    }

    override fun syncRRMMGeneral(c: Int, u: Int): Observable<List<RptGeneral>> {
        return apiService.getRRMMGeneral(c, u)
    }

    override fun syncRRMMDiario(c: Int, u: Int): Observable<List<RptDiario>> {
        return apiService.getRRMMDiario(c, u)
    }

    override fun clearRptGeneral(): Completable {
        return Completable.fromAction {
            dataBase.rptGeneralDao().deleteAll()
        }
    }

    override fun clearRptDiario(): Completable {
        return Completable.fromAction {
            dataBase.rptDiarioDao().deleteAll()
        }
    }

    override fun insertRptGeneral(g: List<RptGeneral>): Completable {
        return Completable.fromAction {
            for (p: RptGeneral in g) {
                val id = dataBase.rptGeneralDao().getMaxIdRpt()
                p.id = if (id == 0) 1 else id + 1
                dataBase.rptGeneralDao().insertRptGeneralTask(p)
            }
        }
    }

    override fun insertRptDiario(g: List<RptDiario>): Completable {
        return Completable.fromAction {
            for (p: RptDiario in g) {
                val id = dataBase.rptDiarioDao().getMaxIdRpt()
                p.id = if (id == 0) 1 else id + 1
                dataBase.rptDiarioDao().insertRptDiarioTask(p)
            }
        }
    }

    override fun syncSUPGeneral(c: Int, u: Int): Observable<List<RptGeneral>> {
        return apiService.getSUPGeneral(c, u)
    }

    override fun syncSUPDiario(c: Int, u: Int): Observable<List<RptDiario>> {
        return apiService.getSUPDiario(c, u)
    }

    override fun getRptGeneralCabecera(): LiveData<RptGeneral> {
        return dataBase.rptGeneralDao().getRptGeneralCabecera()
    }

    override fun getRptGeneral(): LiveData<List<RptGeneral>> {
        return dataBase.rptGeneralDao().getRptGeneral()
    }

    override fun getRptDiarioCabecera(): LiveData<RptDiario> {
        return dataBase.rptDiarioDao().getRptDiarioCabecera()
    }

    override fun getRptDiario(): LiveData<List<RptDiario>> {
        return dataBase.rptDiarioDao().getRptDiario()
    }

    override fun verificateVisitaMedico(medicoId: Int, fecha: String): Observable<Mensaje> {
        return apiService.getVerificateVisitaMedico(medicoId, fecha)
    }

    override fun sendMedicoCabecera(m: Medico): Observable<Mensaje> {
        val json = Gson().toJson(m)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.saveMedicoCabecera(body)
    }

    override fun sendMedicoDireccion(m: MedicoDireccion): Observable<Mensaje> {
        val json = Gson().toJson(m)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.saveMedicoDireccion(body)
    }

    override fun syncBirthDay(u: Int, m: Int): Observable<List<BirthDay>> {
        return apiService.getBirthDay(u, m)
    }

    override fun insertBirthDays(p: List<BirthDay>): Completable {
        return Completable.fromAction {
            dataBase.birthDayDao().insertBirthDayListTask(p)
        }
    }

    override fun getBirthDays(usuarioId: Int, mes: Int): LiveData<List<BirthDay>> {
        return if (mes == 0) {
            dataBase.birthDayDao().getBirthDays()
        } else {
            dataBase.birthDayDao().getBirthDaysByMonth(mes)
        }
    }
}