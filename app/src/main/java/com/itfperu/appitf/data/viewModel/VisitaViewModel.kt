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

class VisitaViewModel @Inject
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

    fun syncVisita() {
        roomRepository.clearVisita()
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
        roomRepository.syncVisita()
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Visita>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: List<Visita>) {
                    insertVisitas(t)
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {}
            })
    }

    private fun insertVisitas(p: List<Visita>) {
        roomRepository.insertVisitas(p)
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


    fun getVisitas(): LiveData<List<Visita>> {
        return roomRepository.getVisitas()
    }

    fun validateVisita(c: Visita) {


        verificateVisita(c)
    }


    private fun verificateVisita(c: Visita) {
        roomRepository.verificateVisita(c)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    sendVisita(c)
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }


    private fun sendVisita(c: Visita) {
        roomRepository.sendVisita(c)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: Mensaje) {
                    insertVisita(c, t)
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {}
            })
    }

    private fun insertVisita(c: Visita, m: Mensaje) {
        roomRepository.insertVisita(c, m)
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

    fun getVisitaById(visitaId: Int): LiveData<Visita> {
        return roomRepository.getVisitaById(visitaId)
    }
}