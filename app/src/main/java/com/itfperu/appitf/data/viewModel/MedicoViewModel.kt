package com.itfperu.appitf.data.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.itfperu.appitf.data.local.model.*
import com.itfperu.appitf.data.local.repository.ApiError
import com.itfperu.appitf.data.local.repository.AppRepository
import com.itfperu.appitf.helper.Mensaje
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MedicoViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) :
    ViewModel() {

    val mensajeError = MutableLiveData<String>()
    val mensajeSuccess = MutableLiveData<String>()
    val search = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun setError(s: String) {
        mensajeError.value = s
    }

    fun setLoading(s: Boolean) {
        loading.value = s
    }

    fun syncSolMedico(u: Int, fi: String, ff: String, e: Int, t: Int) {
        roomRepository.syncSolMedico(u, fi, ff, e, t)
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<SolMedico>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: List<SolMedico>) {
                    insertSolMedicos(t)
                }

                override fun onError(t: Throwable) {
                    if (t is HttpException) {
                        val body = t.response().errorBody()
                        try {
                            val error = retrofit.errorConverter.convert(body!!)
                            mensajeError.postValue(error.Message)
                        } catch (e1: IOException) {
                            e1.printStackTrace()
                        }
                    } else {
                        mensajeError.postValue(t.message)
                    }
                    loading.value = false
                }

                override fun onComplete() {

                }
            })
    }

    private fun insertSolMedicos(p: List<SolMedico>) {
        roomRepository.insertSolMedicos(p)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onComplete() {
                    loading.value = false
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                    loading.value = false
                }
            })
    }

    fun getMedicos(): LiveData<List<SolMedico>> {
        return Transformations.switchMap(search) { input ->
            val f = Gson().fromJson(search.value, Filtro::class.java)
            roomRepository.getSolMedicos(f.usuarioId, f.finicio, f.ffinal, f.estadoId, f.tipo)
        }
    }


    fun validateSolMedico(c: SolMedico) {
        insertSolMedico(c)
    }

    private fun insertSolMedico(c: SolMedico) {
        roomRepository.insertSolMedicoCab(c)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}

                override fun onComplete() {
                    mensajeSuccess.value = "Medico Guardado"
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    private fun insertSolMedico(c: SolMedico, m: Mensaje) {
        roomRepository.insertSolMedico(c, m)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    mensajeSuccess.value = "Guardado"
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun getSolMedicoById(categoriaId: Int): LiveData<SolMedico> {
        return roomRepository.getSolMedicoById(categoriaId)
    }

    fun getCiclos(): LiveData<List<Ciclo>> {
        return roomRepository.getCiclos()
    }

    fun getCicloProceso(): LiveData<List<Ciclo>> {
        return roomRepository.getCicloProceso()
    }

    fun getEstados(tipo: String): LiveData<List<Estado>> {
        return roomRepository.getEstados(tipo)
    }

    fun getDuracion(): LiveData<List<Duracion>> {
        return roomRepository.getDuracion()
    }

    fun getUsuarios(): LiveData<List<Personal>> {
        return roomRepository.getPersonals()
    }

    fun getMedicoById(medicoId: Int): LiveData<Medico> {
        return roomRepository.getMedicoById(medicoId)
    }

    fun validateMedico(m: Medico) {
        if (m.identificadorId == 0) {
            mensajeError.value = "Seleccione Identificador"
            return
        }
        if (m.cpmMedico.isEmpty()) {
            mensajeError.value = "Ingrese CMP"
            return
        }
        if (m.nombreMedico.isEmpty()) {
            mensajeError.value = "Ingrese Nombre"
            return
        }
        if (m.apellidoP.isEmpty()) {
            mensajeError.value = "Ingrese Apellido Paterno"
            return
        }
        if (m.apellidoM.isEmpty()) {
            mensajeError.value = "Ingrese Apellido Materno"
            return
        }
        if (m.categoriaId == 0) {
            mensajeError.value = "Seleccione Categoria"
            return
        }
        if (m.especialidadId == 0) {
            mensajeError.value = "Seleccione Especialidad 1"
            return
        }
        if (m.sexo.isEmpty()) {
            mensajeError.value = "Seleccione Sexo"
            return
        }
        insertMedico(m)
    }

    private fun insertMedico(m: Medico) {
        roomRepository.insertMedico(m)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    mensajeSuccess.value = "Guardado"
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun getIdentificadores(): LiveData<List<Identificador>> {
        return roomRepository.getIdentificadores()
    }

    fun getCategorias(): LiveData<List<Categoria>> {
        return roomRepository.getCategorias()
    }

    fun getEspecialidades(): LiveData<List<Especialidad>> {
        return roomRepository.getEspecialidads()
    }

    fun getDepartamentos(): LiveData<List<Ubigeo>> {
        return roomRepository.getDepartamentos()
    }

    fun getProvincias(cod: String): LiveData<List<Ubigeo>> {
        return roomRepository.getProvincias(cod)
    }

    fun getDistritos(cod: String, cod2: String): LiveData<List<Ubigeo>> {
        return roomRepository.getDistritos(cod, cod2)
    }

    fun getDireccionesById(id: Int): LiveData<List<MedicoDireccion>> {
        return roomRepository.getDireccionesById(id)
    }

    fun validateDireccion(m: MedicoDireccion): Boolean {
        if (m.codigoDepartamento.isEmpty()) {
            mensajeError.value = "Seleccione Departamento"
            return false
        }
        if (m.codigoProvincia.isEmpty()) {
            mensajeError.value = "Seleccione Provincia"
            return false
        }
        if (m.codigoDistrito.isEmpty()) {
            mensajeError.value = "Seleccione Distrito"
            return false
        }
        if (m.direccion.isEmpty()) {
            mensajeError.value = "Ingrese Dirección"
            return false
        }
        if (m.institucion.isEmpty()) {
            mensajeError.value = "Ingrese Institución"
            return false
        }
        if (m.direccion.isEmpty()) {
            mensajeError.value = "Ingrese Dirección"
            return false
        }
        insertDireccion(m)
        return true
    }

    private fun insertDireccion(m: MedicoDireccion) {
        roomRepository.insertDireccion(m)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    mensajeSuccess.value = "Dirección Guardado"
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun getMedicosById(solMedicoId: Int): LiveData<List<Medico>> {
        return roomRepository.getMedicosById(solMedicoId)
    }

    fun getMedicoId(): LiveData<Int> {
        return roomRepository.getMedicoId()
    }

    fun getSolMedicoId(): LiveData<Int> {
        return roomRepository.getSolMedicoId()
    }

    fun deleteMedico(m: Medico) {
        roomRepository.deleteMedico(m)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    mensajeSuccess.value = "Eliminado"
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun sendMedicos(tipoMedico: Int) {
        val ots: Observable<List<SolMedico>> = roomRepository.getSolMedicoTask(tipoMedico)
        ots.flatMap { observable ->
            Observable.fromIterable(observable).flatMap { a ->
                val json = Gson().toJson(a)
                Log.i("TAG", json)
                val body =
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
                Observable.zip(
                    Observable.just(a), roomRepository.sendSolMedico(body), { _, mensaje ->
                        mensaje
                    })
            }
        }.subscribeOn(Schedulers.io())
            .delay(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {
                override fun onComplete() {
                    mensajeSuccess.value = "Enviado"
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: Mensaje) {
                    updateEnabledMedico(t)
                }

                override fun onError(t: Throwable) {
                    if (t is HttpException) {
                        val body = t.response().errorBody()
                        try {
                            val error = retrofit.errorConverter.convert(body!!)
                            mensajeError.postValue(error.Message)
                        } catch (e1: IOException) {
                            e1.printStackTrace()
                        }
                    } else {
                        mensajeError.postValue(t.message)
                    }
                }
            })
    }

    private fun updateEnabledMedico(t: Mensaje) {
        roomRepository.updateEnabledMedico(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun deleteDireccion(m: MedicoDireccion) {
        roomRepository.deleteDireccion(m)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    mensajeError.value = "Dirección eliminada"
                }

                override fun onError(e: Throwable) {}
            })
    }

    fun getDireccionById(id: Int): LiveData<MedicoDireccion> {
        return roomRepository.getDireccionById(id)
    }

    fun getSolMedicoCab(solMedicoId: Int): LiveData<SolMedico> {
        return roomRepository.getSolMedicoCab(solMedicoId)
    }
}