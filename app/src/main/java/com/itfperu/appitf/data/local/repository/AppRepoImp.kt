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
import com.itfperu.appitf.helper.MensajeDetalleDet
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
//            val a: List<Accesos>? = u.accesos
//            if (a != null) {
//                dataBase.accesosDao().insertAccesosListTask(a)
//            }
        }
    }

    override fun deleteSesion(): Completable {
        return Completable.fromAction {
            dataBase.usuarioDao().deleteAll()
            dataBase.cicloDao().deleteAll()
            dataBase.estadoDao().deleteAll()
            dataBase.duracionDao().deleteAll()
            dataBase.personalDao().deleteAll()
            dataBase.categoriaDao().deleteAll()
            dataBase.identificadorDao().deleteAll()
            dataBase.especialidadDao().deleteAll()
            dataBase.departamentoDao().deleteAll()
            dataBase.provinciaDao().deleteAll()
            dataBase.distritoDao().deleteAll()
            dataBase.medicoDao().deleteAll()

            dataBase.actividadDao().deleteAll()
            dataBase.targetDao().deleteAll()
            dataBase.targetCabDao().deleteAll()
            dataBase.targetDetDao().deleteAll()

            dataBase.solMedicoDao().deleteAll()
            dataBase.medicoDao().deleteAll()
            dataBase.medicoDireccionDao().deleteAll()
        }
    }

    override fun deleteSync(): Completable {
        return Completable.fromAction {

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
            val dp: List<Departamento>? = s.departamentos
            if (dp != null) {
                dataBase.departamentoDao().insertDepartamentoListTask(dp)
            }
            val pr: List<Provincia>? = s.provincias
            if (pr != null) {
                dataBase.provinciaDao().insertProvinciaListTask(pr)
            }
            val di: List<Distrito>? = s.distritos
            if (di != null) {
                dataBase.distritoDao().insertDistritoListTask(di)
            }
            val me: List<Medico>? = s.medicos
            if (me != null) {
                dataBase.medicoDao().insertMedicoListTask(me)
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

    override fun syncActividad(u: Int, c: Int, e: Int, t: Int): Observable<List<Actividad>> {
        return apiService.getActividad(u, c, e, t)
    }

    override fun insertActividads(p: List<Actividad>): Completable {
        return Completable.fromAction {
            dataBase.actividadDao().insertActividadListTask(p)
        }
    }

    override fun getActividads(tipo: Int): LiveData<List<Actividad>> {
        return dataBase.actividadDao().getActividades(tipo)
    }

    override fun sendActividad(body: RequestBody): Observable<Mensaje> {
        return apiService.saveActividad(body)
    }

    override fun insertActividad(c: Actividad): Completable {
        return Completable.fromAction {
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
            dataBase.solMedicoDao().insertMedicoListTask(p)
            for (a: SolMedico in p) {
                val b: List<Medico>? = a.medicos
                if (b != null) {
                    dataBase.medicoDao().insertMedicoListTask(b)
                    for (c: Medico in b) {
                        val d: List<MedicoDireccion>? = c.direcciones
                        if (d != null) {
                            dataBase.medicoDireccionDao().insertMedicoDireccionListTask(d)
                        }
                    }
                }
            }
        }
    }

    override fun getSolMedicos(tipoMedico: Int): LiveData<List<SolMedico>> {
        return dataBase.solMedicoDao().getMedicos(tipoMedico)
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

    override fun getSolMedicoTask(tipoMedico:Int): Observable<List<SolMedico>> {
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
                    m.direcciones = dataBase.medicoDireccionDao().getDireccionIdTask(m.medicoId)
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

            val d = t.detalle
            if (d != null) {
                for (m: MensajeDetalle in d) {
                    dataBase.medicoDao().updateEnabledMedico(m.detalleId, m.detalleRetornoId)
                    val e = m.subDetalles
                    if (e != null) {
                        for (dt: MensajeDetalleDet in e) {
                            dataBase.medicoDireccionDao()
                                .updateEnabledDireccion(dt.codigoBase, dt.codigoRetorno)
                        }
                    }
                }
            }
        }
    }

    override fun getMedicoById(id: Int): LiveData<Medico> {
        return dataBase.medicoDao().getMedicoById(id)
    }

    override fun insertMedico(m: Medico): Completable {
        return Completable.fromAction {
            val e: Medico? = dataBase.medicoDao().getMedicoByIdTask(m.medicoId)
            if (e == null) {
                val a: Medico? =
                    dataBase.medicoDao().getMedicoCmpIdentificador(m.cpmMedico, m.identificadorId)
                if (a != null) {
                    error("Ya existe un medico con el mismo CMP e Identificador")
                } else {
                    dataBase.medicoDao().insertMedicoTask(m)
                }
            } else {
                val a = dataBase.medicoDireccionDao().getMedicoDirecciones(e.medicoId)
                if (a > 0) {
                    m.active = 1
                }
                dataBase.medicoDao().updateMedicoTask(m)
            }
        }
    }

    override fun getIdentificadores(): LiveData<List<Identificador>> {
        return dataBase.identificadorDao().getIdentificadores()
    }

    override fun getDepartamentos(): LiveData<List<Departamento>> {
        return dataBase.departamentoDao().getDepartamentos()
    }

    override fun getProvincias(): LiveData<List<Provincia>> {
        return dataBase.provinciaDao().getProvincias()
    }

    override fun getDistritos(): LiveData<List<Distrito>> {
        return dataBase.distritoDao().getDistritos()
    }

    override fun getMedicos(): LiveData<List<Medico>> {
        return dataBase.medicoDao().getMedicos()
    }

    override fun getDireccionesById(id: Int): LiveData<List<MedicoDireccion>> {
        return dataBase.medicoDireccionDao().getMedicoDireccionesById(id)
    }

    override fun insertDireccion(m: MedicoDireccion): Completable {
        return Completable.fromAction {
            dataBase.medicoDao().closeMedico(m.medicoId)

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

    override fun insertSolMedicoCab(c: SolMedico): Completable {
        return Completable.fromAction {
            val list = dataBase.medicoDao().getMedicosInactivos(c.solMedicoId)
            if (list > 0) {
                error("Cada Medico debe de tener una direccion minimo")
            } else {
                c.usuario = dataBase.usuarioDao().getUsuarioNombre()
                val a: SolMedico? = dataBase.solMedicoDao().getSolMedicoByIdTask(c.solMedicoId)
                if (a == null)
                    dataBase.solMedicoDao().insertMedicoTask(c)
                else
                    dataBase.solMedicoDao().updateMedicoTask(c)
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

    override fun syncTarget(u: Int, c: Int, e: Int, n: Int): Observable<List<TargetM>> {
        return apiService.getTargets(u, c, e, n)
    }

    override fun insertTargets(p: List<TargetM>): Completable {
        return Completable.fromAction {
            dataBase.targetDao().insertTargetListTask(p)
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

    override fun getTargetsAltas(t: String,tipo:Int): LiveData<List<TargetCab>> {
        return dataBase.targetCabDao().getTargetsAltas(t,tipo)
    }

    override fun syncTargetCab(
        u: Int, fi: String, ff: String, e: Int, tt: String, t: Int
    ): Observable<List<TargetCab>> {
        return apiService.getTargetsCab(u, fi, ff, e, tt, t)
    }

    override fun insertTargetsCab(p: List<TargetCab>): Completable {
        return Completable.fromAction {
            dataBase.targetCabDao().insertTargetCabListTask(p)
            for (a: TargetCab in p) {
                val b: List<TargetDet>? = a.detalle
                if (b != null) {
                    dataBase.targetDetDao().insertTargetDetListTask(b)
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

    override fun insertTargetDet(d: TargetDet): Completable {
        return Completable.fromAction {
            val a: TargetDet? = dataBase.targetDetDao().getTargetDetByIdTask(d.targetDetId)
            if (a == null)
                dataBase.targetDetDao().insertTargetDetTask(d)
            else
                dataBase.targetDetDao().updateTargetDetTask(d)
        }
    }

    override fun insertTargetCab(c: TargetCab): Completable {
        return Completable.fromAction {
            c.usuarioSolicitante = dataBase.usuarioDao().getUsuarioNombre()

            val a: TargetCab? = dataBase.targetCabDao().getTargetCabByIdTask(c.targetCabId)
            if (a == null)
                dataBase.targetCabDao().insertTargetCabTask(c)
            else
                dataBase.targetCabDao().updateTargetCabTask(c)
        }
    }

    override fun getTargetCabTask(tipoTarget: String): Observable<List<TargetCab>> {
        return Observable.create { e ->
            val a: ArrayList<TargetCab> = ArrayList()
            val data: List<TargetCab> = dataBase.targetCabDao().getTargetCabTask(tipoTarget)
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
        }
    }

    override fun getCheckMedicos(): LiveData<PagedList<Medico>> {
        return dataBase.medicoDao().getCheckMedicos().toLiveData(
            Config(pageSize = 20, enablePlaceholders = true)
        )
    }

    override fun getCheckMedicos(s: String): LiveData<PagedList<Medico>> {
        return dataBase.medicoDao().getCheckMedicos(s).toLiveData(
            Config(pageSize = 20, enablePlaceholders = true)
        )
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
                a.nombreMedico = s.nombreMedico
                a.nroContacto = 0
                a.tipoTarget = tipoTarget

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
}