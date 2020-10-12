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
    val loading = MutableLiveData<Boolean>()
    val searchMedico: MutableLiveData<String> = MutableLiveData()

    fun setError(s: String) {
        mensajeError.value = s
    }

    fun setLoading(s: Boolean) {
        loading.value = s
    }

    fun syncTarget(u: Int, c: Int, e: Int, n: Int) {
        roomRepository.syncTarget(u, c, e, n)
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<TargetM>> {
                override fun onSubscribe(d: Disposable) {

                }

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

                override fun onComplete() {

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

    fun getTargetsAltas(tipoTarget: String,tipo:Int): LiveData<List<TargetCab>> {
        return roomRepository.getTargetsAltas(tipoTarget,tipo)
    }

    fun syncTargetCab(u: Int, fi: String, ff: String, e: Int, tt: String, t: Int) {
        roomRepository.syncTargetCab(u, fi, ff, e, tt, t)
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

    fun sendTarges(tipoTarget: String) {
        val ots: Observable<List<TargetCab>> = roomRepository.getTargetCabTask(tipoTarget)
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

    fun getCheckMedicos(): LiveData<PagedList<Medico>> {
        return Transformations.switchMap(searchMedico) { input ->
            if (input == null || input.isEmpty()) {
                roomRepository.getCheckMedicos()
            } else {
                roomRepository.getCheckMedicos(String.format("%s%s%s", "%", input, "%"))
            }
        }
    }

    fun updateCheckMedico(s: Medico) {
        roomRepository.updateCheckMedico(s)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message.toString()
                }
            })
    }
}