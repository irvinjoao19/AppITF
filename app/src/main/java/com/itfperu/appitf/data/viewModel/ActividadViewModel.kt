package com.itfperu.appitf.data.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
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

class ActividadViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) :
    ViewModel() {

    val mensajeError = MutableLiveData<String>()
    val mensajeSuccess = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    val search: MutableLiveData<String> = MutableLiveData()


    fun setError(s: String) {
        mensajeError.value = s
    }

    fun setLoading(s: Boolean) {
        loading.value = s
    }

    fun syncActividad(u: Int, c: Int, e: Int, t: Int) {
        roomRepository.syncActividad(u, c, e, t)
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Actividad>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: List<Actividad>) {
                    insertActividads(t)
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

    private fun insertActividads(p: List<Actividad>) {
        roomRepository.insertActividads(p)
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

    fun getActividades(): LiveData<List<Actividad>> {
        return Transformations.switchMap(search) { input ->
            val f = Gson().fromJson(search.value, Filtro::class.java)
            roomRepository.getActividades(f.usuarioId, f.cicloId, f.estadoId, f.tipo)
        }
    }

    // t  =  1 -> actividades 2 -> aprobacion
    fun validateActividad(c: Actividad) {
        if (c.cicloId == 0) {
            mensajeError.value = "Seleccione Ciclo"
            return
        }
        if (c.fechaActividad.isEmpty()) {
            mensajeError.value = "Seleccione Fecha de Actividad"
            return
        }
        if (c.duracionId == 0) {
            mensajeError.value = "Seleccione Duración"
            return
        }

        if (c.tipo == 1) {
            if (c.estado == 8 || c.estado == 9) {
                mensajeError.value = "No se puede modificar"
                return
            }
        }

        insertActividad(c)
    }

    private fun insertActividad(c: Actividad) {
        roomRepository.insertActividad(c)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onComplete() {
                    mensajeSuccess.value = "Guardado"
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }


    fun sendActividad(tipo: Int) {
        val ots: Observable<List<Actividad>> = roomRepository.getActividadTask(tipo)
        ots.flatMap { observable ->
            Observable.fromIterable(observable).flatMap { a ->
                val json = Gson().toJson(a)
                Log.i("TAG", json)
                val body =
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
                Observable.zip(
                    Observable.just(a), roomRepository.sendActividad(body), { _, mensaje ->
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
                    updateEnabledActividad(t)
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

    private fun updateEnabledActividad(t: Mensaje) {
        roomRepository.updateEnabledActividad(t)
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


    fun getActividadById(categoriaId: Int): LiveData<Actividad> {
        return roomRepository.getActividadById(categoriaId)
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

    fun getNombreUsuario(): LiveData<String> {
        return roomRepository.getNombreUsuario()
    }

//    fun getActividades(): LiveData<List<Actividad>> {
//        return actividades
//    }
}