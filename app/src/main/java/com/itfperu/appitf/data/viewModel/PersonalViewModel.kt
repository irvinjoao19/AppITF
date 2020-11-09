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

class PersonalViewModel @Inject
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

    fun syncPersonal() {
        roomRepository.clearPersonal()
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
        roomRepository.syncPersonal()
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Personal>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: List<Personal>) {
                    insertPersonals(t)
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

    private fun insertPersonals(p: List<Personal>) {
        roomRepository.insertPersonals(p)
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


    fun getPersonals(): LiveData<List<Personal>> {
        return roomRepository.getPersonals()
    }

    fun validatePersonal(c: Personal) {
        if (c.nombre.isEmpty()) {
            mensajeError.value = "Debe ingresar el nombre del usuario"
            return
        }
        if (c.apellidoP.isEmpty()) {
            mensajeError.value = "Debe ingresar el apellido paterno"
            return
        }
        if (c.apellidoM.isEmpty()) {
            mensajeError.value = "Debe ingresar el apellido materno"
            return
        }
        if (c.login.isEmpty()) {
            mensajeError.value = "Debe ingresar el login"
            return
        }
        if (c.pass.isEmpty()) {
            mensajeError.value = "Debe ingresar la contraseña"
            return
        }
        if (c.perfilId == 0) {
            mensajeError.value = "Debe elegir un Rol"
            return
        }
        verificatePersonal(c)
    }

    private fun verificatePersonal(c: Personal) {
        roomRepository.verificatePersonal(c)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    sendPersonal(c)
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun sendPersonal(c: Personal) {
        roomRepository.sendPersonal(c)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: Mensaje) {
                    insertPersonal(c, t)
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {}
            })
    }

    private fun insertPersonal(c: Personal, m: Mensaje) {
        roomRepository.insertPersonal(c, m)
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

    fun getPersonalById(perfilId: Int): LiveData<Personal> {
        return roomRepository.getPersonalById(perfilId)
    }

    fun getSupervisores(): LiveData<List<Personal>> {
        return roomRepository.getSupervisores()
    }
}