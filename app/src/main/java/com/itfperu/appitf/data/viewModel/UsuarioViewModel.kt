package com.itfperu.appitf.data.viewModel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.itfperu.appitf.data.local.model.*
import com.itfperu.appitf.data.local.repository.ApiError
import com.itfperu.appitf.data.local.repository.AppRepository
import com.itfperu.appitf.helper.Mensaje
import com.itfperu.appitf.helper.Util
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UsuarioViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) :
    ViewModel() {

    val mensajeError = MutableLiveData<String>()
    val mensajeSuccess = MutableLiveData<String>()

    val user: LiveData<Usuario>
        get() = roomRepository.getUsuario()

    fun setError(s: String) {
        mensajeError.value = s
    }

    fun getLogin(usuario: String, pass: String, imei: String, version: String) {
        roomRepository.getUsuarioService(usuario, pass, imei, version)
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Usuario> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(usuario: Usuario) {
                    insertUsuario(usuario, version)
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

                override fun onComplete() {
                }
            })
    }

    fun insertUsuario(u: Usuario, v: String) {
        roomRepository.insertUsuario(u)
            .delay(3, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onComplete() {
                    mensajeSuccess.value = "Login"
////                    getSync(u, v)
//                    sync(u.usuarioId, 0, 0)
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    fun logout(login: String) {
        deleteUser(login)
//        var mensaje = ""
//        roomRepository.getLogout(login)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : Observer<Mensaje> {
//                override fun onSubscribe(d: Disposable) {
//
//                }
//
//                override fun onNext(m: Mensaje) {
//                    mensaje = m.mensaje
//                }
//
//                override fun onError(t: Throwable) {
//                    if (t is HttpException) {
//                        val body = t.response().errorBody()
//                        try {
//                            val error = retrofit.errorConverter.convert(body!!)
//                            mensajeError.postValue(error.Message)
//                        } catch (e1: IOException) {
//                            e1.printStackTrace()
//                        }
//                    } else {
//                        mensajeError.postValue(t.message)
//                    }
//                }
//
//                override fun onComplete() {
//                    deleteUser(mensaje)
//                }
//            })
    }


    private fun deleteUser(mensaje: String) {
        roomRepository.deleteSesion()
            .delay(2, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onComplete() {
                    mensajeSuccess.value = "Close"
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    fun sync(u: Int) {
        roomRepository.deleteSync()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    roomRepository.getSync(u)
                        .delay(1000, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<Sync> {
                            override fun onSubscribe(d: Disposable) {}
                            override fun onComplete() {}
                            override fun onNext(t: Sync) {
                                insertSync(t)
                            }

                            override fun onError(e: Throwable) {
                                if (e is HttpException) {
                                    val body = e.response().errorBody()
                                    try {
                                        val error = retrofit.errorConverter.convert(body!!)
                                        mensajeError.postValue(error.Message)
                                    } catch (e1: IOException) {
                                        e1.printStackTrace()
                                        Log.i("TAG", e1.toString())
                                    }
                                } else {
                                    mensajeError.postValue(e.toString())
                                }
                            }
                        })
                }
            })
    }

    private fun insertSync(p: Sync) {
        roomRepository.saveSync(p)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onComplete() {
                    mensajeSuccess.value = "Sincronización Completa"
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun getUsuarioById(id: Int): LiveData<Usuario> {
        return roomRepository.getUsuarioById(id)
    }

    fun generarArchivo(nameImg: String, context: Context, data: Intent) {
        Util.getFolderAdjunto(nameImg, context, data)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    mensajeSuccess.value = nameImg
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.i("TAG", e.toString())
                }
            })
    }

    fun updateUsuario(u: Usuario, context: Context) {
        roomRepository.updateUsuario(u)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    sendData(context)
                }

                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Log.i("TAG", e.toString())
                }
            })
    }

    fun sendData(context: Context) {
        val ots: Observable<Usuario> = roomRepository.getUsuarioTask()
        ots.flatMap { u ->
            val b = MultipartBody.Builder()
            if (u.foto.isNotEmpty()) {
                val file = File(Util.getFolder(context), u.foto)
                if (file.exists()) {
                    b.addFormDataPart(
                        "files", file.name,
                        RequestBody.create(MediaType.parse("multipart/form-data"), file)
                    )
                }
            }
            val json = Gson().toJson(u)
            Log.i("TAG", json)
            b.setType(MultipartBody.FORM)
            b.addFormDataPart("data", json)

            val body = b.build()
            Observable.zip(
                Observable.just(u), roomRepository.sendUsuario(body), { _, mensaje ->
                    mensaje
                })
        }.subscribeOn(Schedulers.io())
            .delay(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {
                override fun onComplete() {
                    mensajeSuccess.value = "Actualizado"
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Mensaje) {

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
            })
    }

    fun getAccesos(usuarioId: Int): LiveData<List<Accesos>> {
        return roomRepository.getAccesos(usuarioId)
    }
}