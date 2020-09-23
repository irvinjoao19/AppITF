package com.itfperu.appitf.data.local.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.itfperu.appitf.data.local.AppDataBase
import com.itfperu.appitf.data.local.model.*
import com.itfperu.appitf.helper.Mensaje
import com.google.gson.Gson
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

        }
    }

    override fun deleteSync(): Completable {
        return Completable.fromAction {

        }
    }

    override fun getSync(u: Int, e: Int, p: Int): Observable<Sync> {
        val f = Filtro("", "", "", "")
        val json = Gson().toJson(f)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.getSync(body)
    }

    override fun saveSync(s: Sync): Completable {
        return Completable.fromAction {
//            val m: List<Material>? = s.materials
//            if (m != null) {
//                dataBase.materialDao().insertMaterialListTask(m)
//            }
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
            if (c.codigo == "1") {
                Throwable("Error de Codigo")
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

    override fun removePerfil(i: Int): Observable<Mensaje> {
        return apiService.removePerfil(i)
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

    override fun removeMoneda(i: Int): Observable<Mensaje> {
        return apiService.removeMoneda(i)
    }

    override fun deleteMoneda(m: Moneda): Completable {
        return Completable.fromAction {
            m.estado = "INACTIVO"
            m.estadoId = 0
            dataBase.monedaDao().updateMonedaTask(m)
        }
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

    override fun removeCategoria(i: Int): Observable<Mensaje> {
        return apiService.removeCategoria(i)
    }

    override fun deleteCategoria(c: Categoria): Completable {
        return Completable.fromAction {
            c.estado = "INACTIVO"
            c.estadoId = 0
            dataBase.categoriaDao().updateCategoriaTask(c)
        }
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

    override fun removeEspecialidad(i: Int): Observable<Mensaje> {
        return apiService.removeEspecialidades(i)
    }

    override fun deleteEspecialidad(e: Especialidad): Completable {
        return Completable.fromAction {
            e.estado = "INACTIVO"
            e.estadoId = 0
            dataBase.especialidadDao().updateEspecialidadTask(e)
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


    override fun removeProducto(i: Int): Observable<Mensaje> {
        return apiService.removeProductos(i)
    }

    override fun deleteProducto(p: Producto): Completable {
        return Completable.fromAction {
            p.estado = "INACTIVO"
            p.estadoId = 0
            dataBase.productoDao().updateProductoTask(p)
        }
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

    override fun removeTipoProducto(i: Int): Observable<Mensaje> {
        return apiService.removeTipoProducto(i)
    }

    override fun deleteTipoProducto(t: TipoProducto): Completable {
        return Completable.fromAction {
            t.estado = "INACTIVO"
            t.estadoId = 0
            dataBase.tipoProductoDao().updateTipoProductoTask(t)
        }
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

    override fun removeVisita(i: Int): Observable<Mensaje> {
        return apiService.removeVisita(i)
    }

    override fun deleteVisita(v: Visita): Completable {
        return Completable.fromAction {
            v.estado = "INACTIVO"
            v.estadoId = 0
            dataBase.visitaDao().updateVisitaTask(v)
        }
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
//            if (c. == "1"){
//                Throwable("Error de codigo")
//            }
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

    override fun removeFeriado(i: Int): Observable<Mensaje> {
        return apiService.removeFeriado(i)
    }

    override fun deleteFeriado(f: Feriado): Completable {
        return Completable.fromAction {
            f.estado = "INACTIVO"
            f.estadoId = 0
            dataBase.feriadoDao().updateFeriadoTask(f)
        }
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

    override fun removePersonal(i: Int): Observable<Mensaje> {
        return apiService.removeUsuario(i)
    }

    override fun deletePersonal(p: Personal): Completable {
        return Completable.fromAction {
            p.nombreEstado = "INACTIVO"
            p.estado = 0
            dataBase.personalDao().updatePersonalTask(p)
        }
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

    override fun sendCiclo(c: Ciclo): Observable<Mensaje> {
        val json = Gson().toJson(c)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.saveCiclo(body)
    }

    override fun insertCiclo(c: Ciclo, m: Mensaje): Completable {
        return  Completable.fromAction {
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
}