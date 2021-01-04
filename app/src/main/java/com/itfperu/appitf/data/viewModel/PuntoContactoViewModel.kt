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

class PuntoContactoViewModel @Inject
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

    fun syncPuntoContacto(u: Int, fi: String, ff: String) {
        roomRepository.syncPuntoContacto(u, fi, ff)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<PuntoContacto>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: List<PuntoContacto>) {
                    insertPuntoContactos(t)
                }

                override fun onError(t: Throwable) {
                    if (t is HttpException) {
                        val body = t.response().errorBody()
                        try {
                            val error = retrofit.errorConverter.convert(body!!)
                            mensajeError.postValue(error!!.Message)
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

    private fun insertPuntoContactos(p: List<PuntoContacto>) {
        roomRepository.insertPuntoContactos(p)
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

    fun getPuntoContactos(): LiveData<List<PuntoContacto>> {
        return Transformations.switchMap(search) { input ->
            val f = Gson().fromJson(input, Filtro::class.java)
            roomRepository.getPuntoContactos(f.finicio)
        }
    }

    fun sendPuntoContacto() {
        val ots: Observable<List<PuntoContacto>> =
            roomRepository.getPuntoContactoTask()
        ots.flatMap { observable ->
            Observable.fromIterable(observable).flatMap { a ->
                val json = Gson().toJson(a)
                Log.i("TAG", json)
                val body =
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
                Observable.zip(
                    Observable.just(a), roomRepository.sendPuntoContacto(body), { _, mensaje ->
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
                    updateEnabledPuntoContacto(t)
                }

                override fun onError(t: Throwable) {
                    if (t is HttpException) {
                        val body = t.response().errorBody()
                        try {
                            val error = retrofit.errorConverter.convert(body!!)
                            mensajeError.postValue(error!!.Message)
                        } catch (e1: IOException) {
                            e1.printStackTrace()
                        }
                    } else {
                        mensajeError.postValue(t.toString())
                    }
                }
            })
    }

    private fun updateEnabledPuntoContacto(t: Mensaje) {
        roomRepository.updateEnabledPuntoContacto(t)
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

    fun sendOnlinePuntoContacto(p: PuntoContacto) {
        roomRepository.sendOnlinePuntoContacto(p)
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onNext(t: Mensaje) {
                    insertPuntoContacto(p)
                }

                override fun onError(t: Throwable) {
                    if (t is HttpException) {
                        val body = t.response().errorBody()
                        try {
                            val error = retrofit.errorConverter.convert(body!!)
                            mensajeError.postValue(error!!.Message)
                        } catch (e1: IOException) {
                            e1.printStackTrace()
                        }
                    } else {
                        mensajeError.postValue(t.toString())
                    }
                }
            })
    }

    private fun insertPuntoContacto(p: PuntoContacto) {
        roomRepository.insertPuntoContacto(p)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    mensajeSuccess.value = "Actualizado"
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }
}