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

class TipoProductoViewModel @Inject
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

    fun syncTipoProducto() {
        roomRepository.clearTipoProducto()
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
        roomRepository.syncTipoProducto()
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<TipoProducto>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: List<TipoProducto>) {
                    insertTipoProductos(t)
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {}
            })
    }

    private fun insertTipoProductos(p: List<TipoProducto>) {
        roomRepository.insertTipoProductos(p)
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


    fun getTipoProductos(): LiveData<List<TipoProducto>> {
        return roomRepository.getTipoProductos()
    }

    fun validateTipoProducto(c: TipoProducto) {


        verificateTipoProducto(c)
    }


    private fun verificateTipoProducto(c: TipoProducto) {
        roomRepository.verificateTipoProducto(c)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    sendTipoProducto(c)
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }


    private fun sendTipoProducto(c: TipoProducto) {
        roomRepository.sendTipoProducto(c)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: Mensaje) {
                    insertTipoProducto(c, t)
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {}
            })
    }

    private fun insertTipoProducto(c: TipoProducto, m: Mensaje) {
        roomRepository.insertTipoProducto(c, m)
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

    fun getTipoProductoById(productoId: Int): LiveData<TipoProducto> {
        return roomRepository.getTipoProductoById(productoId)
    }
}