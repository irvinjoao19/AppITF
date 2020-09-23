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

class ProductoViewModel @Inject
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

    fun syncProducto() {
        roomRepository.clearProducto()
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
        roomRepository.syncProducto()
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Producto>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: List<Producto>) {
                    insertProductos(t)
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {}
            })
    }

    private fun insertProductos(p: List<Producto>) {
        roomRepository.insertProductos(p)
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


    fun getProductos(): LiveData<List<Producto>> {
        return roomRepository.getProductos()
    }

    fun validateProducto(c: Producto) {
        if (c.codigo.isEmpty()) {
            mensajeError.value = "Ingrese Codigo Producto"
            return
        }
        if (c.descripcion.isEmpty()) {
            mensajeError.value = "Ingrese Nombre Producto"
            return
        }
        if (c.tipoProductoId == 0) {
            mensajeError.value = "Seleccione Tipo Producto"
            return
        }
        if (c.controlId == 0) {
            mensajeError.value = "Seleccione Control Stock"
            return
        }
        verificateProducto(c)
    }


    private fun verificateProducto(c: Producto) {
        roomRepository.verificateProducto(c)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    sendProducto(c)
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }


    private fun sendProducto(c: Producto) {
        roomRepository.sendProducto(c)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: Mensaje) {
                    insertProducto(c, t)
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

                override fun onComplete() {}
            })
    }

    private fun insertProducto(c: Producto, m: Mensaje) {
        roomRepository.insertProducto(c, m)
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

    fun getProductoById(productoId: Int): LiveData<Producto> {
        return roomRepository.getProductoById(productoId)
    }

    fun syncControl() {
        roomRepository.syncControl()
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Control>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: List<Control>) {
                    insertControls(t)
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {}
            })
    }

    private fun insertControls(t: List<Control>) {
        roomRepository.insertControls(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun getControls(): LiveData<List<Control>> {
        return roomRepository.getControls()
    }

    fun delete(v: Producto) {
        roomRepository.removeProducto(v.productoId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: Mensaje) {
                    deleteObject(v)
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {}
            })
    }

    private fun deleteObject(v: Producto) {
        roomRepository.deleteProducto(v)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    mensajeError.value = "Actualizado"
                }

                override fun onError(e: Throwable) {

                }
            })
    }
}