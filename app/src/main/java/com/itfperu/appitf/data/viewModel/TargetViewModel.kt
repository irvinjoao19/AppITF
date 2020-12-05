package com.itfperu.appitf.data.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.google.gson.Gson
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
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TargetViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) :
    ViewModel() {

    val mensajeError = MutableLiveData<String>()
    val mensajeSuccess = MutableLiveData<String>()
    val mensajeInfo = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    val searchMedico: MutableLiveData<String> = MutableLiveData()
    val search: MutableLiveData<String> = MutableLiveData()
    val perfiles: MutableLiveData<List<ProgramacionPerfil>> = MutableLiveData()
    val rejas: MutableLiveData<List<ProgramacionReja>> = MutableLiveData()
    val detalles: MutableLiveData<List<ProgramacionPerfilDetalle>> = MutableLiveData()

    fun setError(s: String) {
        mensajeError.value = s
    }

    fun setLoading(s: Boolean) {
        loading.value = s
    }

    fun setInfo() {
        mensajeInfo.value = null
    }

    fun setPerfiles() {
        perfiles.value = null
    }

    fun setRejas() {
        rejas.value = null
    }

    fun syncTarget(u: Int, c: Int, e: Int, n: Int, s: String) {
        roomRepository.clearTarget()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    roomRepository.syncTarget(u, c, e, n, s)
                        .delay(1000, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<List<TargetM>> {
                            override fun onSubscribe(d: Disposable) {}
                            override fun onComplete() {}
                            override fun onNext(t: List<TargetM>) {
                                insertTargets(t)
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
                        })
                }
            })
    }

    private fun insertTargets(p: List<TargetM>) {
        roomRepository.insertTargets(p)
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

    fun getTargets(): LiveData<List<TargetM>> {
        return roomRepository.getTargets()
    }

    fun validateTarget(c: TargetCab) {
        insetTargetCab(c)
    }

    private fun insetTargetCab(c: TargetCab) {
        roomRepository.insertTargetCab(c)
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

    private fun insertTarget(c: TargetCab, m: Mensaje) {
        roomRepository.insertTarget(c, m)
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

    fun getTargetById(id: Int): LiveData<TargetM> {
        return roomRepository.getTargetById(id)
    }

    fun getCiclos(): LiveData<List<Ciclo>> {
        return roomRepository.getCiclos()
    }

    fun getCicloProceso(): LiveData<List<Ciclo>> {
        return roomRepository.getCicloProceso()
    }

    fun getEstados(tipo: String): LiveData<List<Estado>> {
        return roomRepository.getEstados(tipo)
    }

    fun getDuracion(): LiveData<List<Duracion>> {
        return roomRepository.getDuracion()
    }

    fun getUsuarios(): LiveData<List<Personal>> {
        return roomRepository.getPersonals()
    }

    fun getCategorias(): LiveData<List<Categoria>> {
        return roomRepository.getCategorias()
    }

    fun getEspecialidades(): LiveData<List<Especialidad>> {
        return roomRepository.getEspecialidads()
    }

    fun getTargetsAltas(tipoTarget: String, tipo: Int): LiveData<List<TargetCab>> {
        return roomRepository.getTargetsAltas(tipoTarget, tipo)
    }

    fun getTargetsAltas(): LiveData<List<TargetCab>> {
        return Transformations.switchMap(search) { input ->
            val f = Gson().fromJson(search.value, Filtro::class.java)
            roomRepository.getTargetsAltas(
                f.usuarioId, f.finicio, f.ffinal, f.estadoId, f.tipo, f.tipoTarget
            )
        }
    }

    fun syncTargetCab(u: Int, fi: String, ff: String, e: Int, tt: String, t: Int, ul: Int) {
        roomRepository.syncTargetCab(u, fi, ff, e, tt, t, ul)
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<TargetCab>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: List<TargetCab>) {
                    insertTargetsCab(t)
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

    private fun insertTargetsCab(p: List<TargetCab>) {
        roomRepository.insertTargetsCab(p)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    loading.value = false
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                    loading.value = false
                }
            })
    }

    fun getMedicos(): LiveData<List<Medico>> {
        return roomRepository.getMedicos()
    }

    fun getTargetDetById(targetId: Int): LiveData<List<TargetDet>> {
        return roomRepository.getTargetDetById(targetId)
    }

    fun getTargetDetId(): LiveData<Int> {
        return roomRepository.getTargetDetId()
    }

    fun getTargetCabId(): LiveData<Int> {
        return roomRepository.getTargetCabId()
    }

    fun validateTargetDet(d: TargetDet) {

        insertTargetDet(d, 0)
    }

    fun insertTargetDet(d: TargetDet, t: Int) {
        roomRepository.insertTargetDet(d)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    if (t == 0) {
                        mensajeSuccess.value = "Guardado"
                    }
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun sendTarges(tipoTarget: String, tipo: Int) {
        val ots: Observable<List<TargetCab>> = roomRepository.getTargetCabTask(tipoTarget, tipo)
        ots.flatMap { observable ->
            Observable.fromIterable(observable).flatMap { a ->
                val json = Gson().toJson(a)
                Log.i("TAG", json)
                val body =
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
                Observable.zip(
                    Observable.just(a), roomRepository.sendTarget(body), { _, mensaje ->
                        mensaje
                    })
            }
        }.subscribeOn(Schedulers.io())
            .delay(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {
                override fun onComplete() {
                    mensajeSuccess.value = "Enviado"
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: Mensaje) {
                    updateEnabledTargetCab(t)
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

    private fun updateEnabledTargetCab(t: Mensaje) {
        roomRepository.updateEnabledTargetCab(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun saveMedico(cabId: Int, tipoTarget: String) {
        roomRepository.saveMedico(cabId, tipoTarget)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    mensajeSuccess.value = "Medicos Agregados"
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun getCheckMedicos(tipoTarget: String, u: Int): LiveData<PagedList<Medico>> {
        return Transformations.switchMap(searchMedico) { input ->
            if (input == null || input.isEmpty()) {
                roomRepository.getCheckMedicos(tipoTarget, u)
            } else {
                roomRepository.getCheckMedicos(
                    tipoTarget,
                    u,
                    String.format("%s%s%s", "%", input, "%")
                )
            }
        }
    }

    fun updateCheckMedico(s: Medico) {
        roomRepository.updateCheckMedico(s)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    mensajeSuccess.value = "Ok"
                }
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {
                    mensajeError.value = e.message.toString()
                }
            })
    }

    fun getTarget(targetId: Int): LiveData<TargetCab> {
        return roomRepository.getTarget(targetId)
    }

    fun getTargetInfo(targetDetId: Int): LiveData<List<TargetInfo>> {
        return roomRepository.getTargetInfo(targetDetId)
    }

    fun updateEstadoTargetDet(t: TargetDet) {
        roomRepository.updateEstadoTargetDet(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {
                    mensajeError.value = e.message.toString()
                }
            })
    }

    fun deleteTargetDet(targetId: Int) {
        roomRepository.deleteTargetDet(targetId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    mensajeSuccess.value = "Operación Cancelada"
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
            })
    }

    fun syncProgramacionPerfil(medicoId: Int) {
        roomRepository.syncProgramacionPerfil(medicoId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<ProgramacionPerfil>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: List<ProgramacionPerfil>) {
                    perfiles.value = t
                }

                override fun onError(t: Throwable) {
                    perfiles.value = null
                    if (t is HttpException) {
                        val body = t.response().errorBody()
                        try {
                            val error = retrofit.errorConverter.convert(body!!)
                            mensajeInfo.postValue(error.Message)
                        } catch (e1: IOException) {
                            e1.printStackTrace()
                        }
                    } else {
                        mensajeInfo.postValue(t.message)
                    }
                }

                override fun onComplete() {}
            })
    }

    fun syncProgramacionPerfilDetalle(medicoId: Int, s: String) {
        roomRepository.syncProgramacionPerfilDetalle(medicoId, s)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<ProgramacionPerfilDetalle>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: List<ProgramacionPerfilDetalle>) {
                    detalles.value = t
                }

                override fun onError(t: Throwable) {
                    detalles.value = null
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
}