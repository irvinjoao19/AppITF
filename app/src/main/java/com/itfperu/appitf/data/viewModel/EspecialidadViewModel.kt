package com.itfperu.appitf.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itfperu.appitf.data.local.model.*
import com.itfperu.appitf.data.local.repository.ApiError
import com.itfperu.appitf.data.local.repository.AppRepository
import com.itfperu.appitf.helper.Mensaje
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class EspecialidadViewModel @Inject
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

    fun syncEspecialidad() {
        roomRepository.clearEspecialidad()
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
        roomRepository.syncEspecialidad()
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Especialidad>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: List<Especialidad>) {
                    insertEspecialidads(t)
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {}
            })
    }

    private fun insertEspecialidads(p: List<Especialidad>) {
        roomRepository.insertEspecialidads(p)
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


    fun getEspecialidads(): LiveData<List<Especialidad>> {
        return roomRepository.getEspecialidads()
    }

    fun validateEspecialidad(c: Especialidad) {
        if (c.codigo.isEmpty()) {
            mensajeError.value = "Debe de ingresar codigo de especialidad"
            return
        }
        if (c.descripcion.isEmpty()) {
            mensajeError.value = "Debe de ingresar descripción de especialidad"
            return
        }
        verificateEspecialidad(c)
    }


    private fun verificateEspecialidad(c: Especialidad) {
        roomRepository.verificateEspecialidad(c)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    sendEspecialidad(c)
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun sendEspecialidad(c: Especialidad) {
        roomRepository.sendEspecialidad(c)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: Mensaje) {
                    insertEspecialidad(c, t)
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {}
            })
    }

    private fun insertEspecialidad(c: Especialidad, m: Mensaje) {
        roomRepository.insertEspecialidad(c, m)
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

    fun getEspecialidadById(especialidadId: Int): LiveData<Especialidad> {
        return roomRepository.getEspecialidadById(especialidadId)
    }
}