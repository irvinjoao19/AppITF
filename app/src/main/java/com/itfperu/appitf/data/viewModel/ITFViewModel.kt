package com.itfperu.appitf.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itfperu.appitf.data.local.model.*
import com.itfperu.appitf.data.local.repository.ApiError
import com.itfperu.appitf.data.local.repository.AppRepository
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

class ITFViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) :
    ViewModel() {

    val mensajeError = MutableLiveData<String>()
    val mensajeSuccess = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    val perfiles = MutableLiveData<List<Perfil>>()
    val monedas = MutableLiveData<List<Moneda>>()
    val feriados = MutableLiveData<List<Feriado>>()

    fun setError(s: String) {
        mensajeError.value = s
    }

    fun setLoading(s: Boolean) {
        loading.value = s
    }

    fun syncPerfiles() {
        roomRepository.syncPerfiles()
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Perfil>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: List<Perfil>) {
                    insertPerfiles(t)
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }
            })
    }

    private fun insertPerfiles(p: List<Perfil>) {
        roomRepository.insertPerfiles(p)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onComplete() {
                    loading.value = false
                    perfiles.value = p
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                    loading.value = false
                }
            })
    }

    fun getPerfiles(): LiveData<List<Perfil>> {
        return perfiles
    }

    fun syncMonedas() {
        roomRepository.syncMonedas()
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Moneda>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: List<Moneda>) {
                    insertMonedas(t)
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }
            })
    }

    private fun insertMonedas(t: List<Moneda>) {
        roomRepository.insertMonedas(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onComplete() {
                    loading.value = false
                    monedas.value = t
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                    loading.value = false
                }
            })
    }

    fun getMonedas(): LiveData<List<Moneda>> {
        return monedas
    }

    fun syncFeriados() {
        roomRepository.syncFeriados()
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Feriado>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: List<Feriado>) {
                    insertFeriados(t)
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }
            })
    }

    private fun insertFeriados(t: List<Feriado>) {
        roomRepository.insertFeriados(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onComplete() {
                    loading.value = false
                    feriados.value = t
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                    loading.value = false
                }
            })
    }

    fun getFeriados(): LiveData<List<Feriado>> {
        return feriados
    }
}