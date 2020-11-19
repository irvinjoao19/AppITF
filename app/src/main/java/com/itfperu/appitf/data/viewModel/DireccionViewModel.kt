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

class DireccionViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) :
    ViewModel() {

    val mensajeError = MutableLiveData<String>()
    val mensajeSuccess = MutableLiveData<String>()
    val mensajeProducto = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    val search: MutableLiveData<String> = MutableLiveData()

    fun setError(s: String) {
        mensajeError.value = s
    }

    fun setErrorProducto(s: String?) {
        mensajeProducto.value = s
    }

    fun setLoading(s: Boolean) {
        loading.value = s
    }

    fun syncDireccion(u: Int, fi: String, ff: String, e: Int, t: Int) {
        roomRepository.syncDireccion(u, fi, ff, e, t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<NuevaDireccion>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: List<NuevaDireccion>) {
                    insertDireccions(t)
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

    private fun insertDireccions(p: List<NuevaDireccion>) {
        roomRepository.insertDireccions(p)
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

    fun getEstados(tipo: String): LiveData<List<Estado>> {
        return roomRepository.getEstados(tipo)
    }

    fun getDirecciones(): LiveData<List<NuevaDireccion>> {
        return Transformations.switchMap(search) { input ->
            if (input == null || input.isEmpty()) {
                roomRepository.getDirecciones()
            } else {
                val f = Gson().fromJson(search.value, Filtro::class.java)
                roomRepository.getDirecciones(
                    f.finicio, f.ffinal, f.estadoId, f.tipo
                )
            }
        }
    }

    fun sendDireccion(tipo: Int) {
        val ots: Observable<List<NuevaDireccion>> =
            roomRepository.getDireccionTask(tipo)
        ots.flatMap { observable ->
            Observable.fromIterable(observable).flatMap { a ->
                val json = Gson().toJson(a)
                Log.i("TAG", json)
                val body =
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
                Observable.zip(
                    Observable.just(a), roomRepository.sendDireccion(body), { _, mensaje ->
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
                    updateEnabledDireccion(t)
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

    private fun updateEnabledDireccion(t: Mensaje) {
        roomRepository.updateEnabledDireccion(t)
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

    fun validateNuevaDireccion(p: NuevaDireccion) {
        if (p.nombreMedico.isEmpty()) {
            mensajeError.value = "Seleccione Medico"
            return
        }
        if (p.nombreDepartamento.isEmpty()) {
            mensajeError.value = "Seleccione departamento"
            return
        }
        if (p.nombreProvincia.isEmpty()) {
            mensajeError.value = "Seleccione provincia"
            return
        }
        if (p.nombreDistrito.isEmpty()) {
            mensajeError.value = "Seleccione distrito"
            return
        }
        if (p.nombreDireccion.isEmpty()) {
            mensajeError.value = "Ingrese Dirección"
            return
        }
        if (p.nombreInstitucion.isEmpty()) {
            mensajeError.value = "Ingrese nombre de institución"
            return
        }
        insertNuevaDireccion(p)
    }

    private fun insertNuevaDireccion(p: NuevaDireccion) {
        roomRepository.insertNuevaDireccion(p)
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

    fun getNuevaDireccionMaxId(): LiveData<Int> {
        return roomRepository.getNuevaDireccionMaxId()
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

    fun getMedicosByEstado(e: Int): LiveData<List<Medico>> {
        return roomRepository.getMedicosByEstado(e)
    }

    fun getNuevaDireccionId(id: Int): LiveData<NuevaDireccion> {
        return roomRepository.getNuevaDireccionId(id)
    }
}