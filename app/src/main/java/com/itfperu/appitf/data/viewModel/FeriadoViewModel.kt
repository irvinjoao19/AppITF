package com.itfperu.appitf.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itfperu.appitf.data.local.model.*
import com.itfperu.appitf.data.local.repository.ApiError
import com.itfperu.appitf.data.local.repository.AppRepository
import com.itfperu.appitf.helper.Mensaje
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FeriadoViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) :
    ViewModel() {

    val mensajeError = MutableLiveData<String>()
    val mensajeSuccess = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun setError(s: String) {
        mensajeError.value = s
    }

    fun setLoading(s: Boolean) {
        loading.value = s
    }

    fun syncFeriado() {
        roomRepository.clearFeriado()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    especialidad()
                }

                override fun onError(e: Throwable) {}
            })
    }

    private fun especialidad() {
        roomRepository.syncFeriado()
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Feriado>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: List<Feriado>) {
                    insertFeriados(t)
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

                override fun onComplete() {}
            })
    }

    private fun insertFeriados(p: List<Feriado>) {
        roomRepository.insertFeriados(p)
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


    fun getFeriados(): LiveData<List<Feriado>> {
        return roomRepository.getFeriados()
    }

    fun validateFeriado(c: Feriado) {
        if (c.fecha.isEmpty()) {
            mensajeError.value = "Seleccione Fecha"
            return
        }
        if (c.descripcion.isEmpty()) {
            mensajeError.value = "Ingrese Nombre de Feriado"
            return
        }
        verificateFeriado(c)
    }

    private fun verificateFeriado(c: Feriado) {
        roomRepository.verificateFeriado(c)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    sendFeriado(c)
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun sendFeriado(c: Feriado) {
        roomRepository.sendFeriado(c)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: Mensaje) {
                    insertFeriado(c, t)
                }

                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }

    private fun insertFeriado(c: Feriado, m: Mensaje) {
        roomRepository.insertFeriado(c, m)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    mensajeSuccess.value = "Save"
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun getFeriadoById(feriadoId: Int): LiveData<Feriado> {
        return roomRepository.getFeriadoById(feriadoId)
    }
}