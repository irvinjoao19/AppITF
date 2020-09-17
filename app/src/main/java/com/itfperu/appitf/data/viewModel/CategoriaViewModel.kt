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

class CategoriaViewModel @Inject
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

    fun syncCategoria() {
        roomRepository.clearCategoria()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onComplete() {
                    categoria()
                }

                override fun onError(e: Throwable) {

                }
            })
    }

    private fun categoria() {
        roomRepository.syncCategoria()
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Categoria>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: List<Categoria>) {
                    insertCategorias(t)
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }
            })
    }

    private fun insertCategorias(p: List<Categoria>) {
        roomRepository.insertCategorias(p)
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


    fun getCategorias(): LiveData<List<Categoria>> {
        return roomRepository.getCategorias()
    }

    fun validateCategoria(c: Categoria) {


        verificateCategoria(c)
    }


    private fun verificateCategoria(c: Categoria) {
        roomRepository.verificateCategoria(c)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onComplete() {
                    sendCategoria(c)
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }


    private fun sendCategoria(c: Categoria) {
        roomRepository.sendCategoria(c)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Mensaje) {
                    insertCategoria(c, t)
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }
            })
    }

    private fun insertCategoria(c: Categoria, m: Mensaje) {
        roomRepository.insertCategoria(c, m)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onComplete() {
                    mensajeSuccess.value = "Save"
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun getCategoriaById(categoriaId: Int): LiveData<Categoria> {
        return roomRepository.getCategoriaById(categoriaId)
    }
}