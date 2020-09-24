package com.itfperu.appitf.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itfperu.appitf.data.local.model.*
import com.itfperu.appitf.data.local.repository.ApiError
import com.itfperu.appitf.data.local.repository.AppRepository
import com.itfperu.appitf.helper.Mensaje
import com.itfperu.appitf.helper.Util
import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CicloViewModel @Inject
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

    fun syncCiclo() {
        roomRepository.clearCiclo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onComplete() {
                    ciclo()
                }

                override fun onError(e: Throwable) {

                }
            })
    }

    private fun ciclo() {
        roomRepository.syncCiclo()
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Ciclo>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: List<Ciclo>) {
                    insertCiclos(t)
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }
            })
    }

    private fun insertCiclos(p: List<Ciclo>) {
        roomRepository.insertCiclos(p)
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


    fun getCiclos(): LiveData<List<Ciclo>> {
        return roomRepository.getCiclos()
    }

    fun validateCiclo(c: Ciclo) {
        if (c.nombre.isEmpty()) {
            mensajeError.value = "Ingrese Nombre de Ciclo"
            return
        }
        if (c.desde.isEmpty()) {
            mensajeError.value = "Seleccione Fecha Desde"
            return
        }
        if (c.hasta.isEmpty()) {
            mensajeError.value = "Seleccione Fecha Hasta"
            return
        }
        if (Util.getCompareFecha(c.desde, c.hasta)) {
            mensajeError.value = "La Fecha Hasta debe ser mayor o igual a la Fecha Desde"
            return
        }
        verificateCiclo(c)
    }

    private fun verificateCiclo(c: Ciclo) {
        roomRepository.verificateCiclo(c)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    sendCiclo(c)
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    private fun sendCiclo(c: Ciclo) {
        roomRepository.sendCiclo(c)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Mensaje) {
                    insertCiclo(c, t)
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }
            })
    }

    private fun insertCiclo(c: Ciclo, m: Mensaje) {
        roomRepository.insertCiclo(c, m)
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

    fun getCicloById(categoriaId: Int): LiveData<Ciclo> {
        return roomRepository.getCicloById(categoriaId)
    }
}