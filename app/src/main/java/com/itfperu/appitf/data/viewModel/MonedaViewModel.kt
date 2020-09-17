package com.itfperu.appitf.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itfperu.appitf.data.local.model.*
import com.itfperu.appitf.data.local.repository.ApiError
import com.itfperu.appitf.data.local.repository.AppRepository
import com.itfperu.appitf.helper.Mensaje
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

class MonedaViewModel @Inject
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

    fun syncMoneda() {
        roomRepository.clearMoneda()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    sync()
                }

                override fun onError(e: Throwable) {}
            })
    }

    private fun sync() {
        roomRepository.syncMoneda()
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Moneda>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: List<Moneda>) {
                    insertMonedas(t)
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {}
            })
    }

    private fun insertMonedas(p: List<Moneda>) {
        roomRepository.insertMonedas(p)
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


    fun getMonedas(): LiveData<List<Moneda>> {
        return roomRepository.getMonedas()
    }

    fun validateMoneda(c: Moneda) {


        verificateMoneda(c)
    }


    private fun verificateMoneda(c: Moneda) {
        roomRepository.verificateMoneda(c)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    sendMoneda(c)
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }


    private fun sendMoneda(c: Moneda) {
        roomRepository.sendMoneda(c)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: Mensaje) {
                    insertMoneda(c, t)
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {}
            })
    }

    private fun insertMoneda(c: Moneda, m: Mensaje) {
        roomRepository.insertMoneda(c, m)
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

    fun getMonedaById(monedaId: Int): LiveData<Moneda> {
        return roomRepository.getMonedaById(monedaId)
    }
}