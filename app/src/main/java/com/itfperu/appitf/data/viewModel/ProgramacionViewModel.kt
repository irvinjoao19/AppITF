package com.itfperu.appitf.data.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
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

class ProgramacionViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) :
    ViewModel() {

    val mensajeError = MutableLiveData<String>()
    val mensajeSuccess = MutableLiveData<String>()
    val mensajeProducto = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    val search: MutableLiveData<String> = MutableLiveData()

    fun setError(s: String) {
        mensajeError.value = s
    }

    fun setErrorProducto(s: String?) {
        mensajeProducto.value = s
    }

    fun setLoading(s: Boolean) {
        loading.value = s
    }

    fun syncProgramacion(u: Int, c: Int) {
        roomRepository.syncProgramacion(u, c)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Programacion>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: List<Programacion>) {
                    insertProgramacions(t)
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

    private fun insertProgramacions(p: List<Programacion>) {
        roomRepository.insertProgramacions(p)
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

    fun getEstados(tipo: String): LiveData<List<Estado>> {
        return roomRepository.getEstados(tipo)
    }

    fun getProgramaciones(): LiveData<List<Programacion>> {
        return Transformations.switchMap(search) { input ->
            if (input == null || input.isEmpty()) {
                roomRepository.getProgramaciones()
            } else {
                val f = Gson().fromJson(search.value, Filtro::class.java)
                roomRepository.getProgramaciones(
                    f.estadoId, f.search
                )
            }
        }
    }


    fun sendProgramacion() {
        val ots: Observable<List<Programacion>> =
            roomRepository.getProgramacionTask()
        ots.flatMap { observable ->
            Observable.fromIterable(observable).flatMap { a ->
                val json = Gson().toJson(a)
                Log.i("TAG", json)
                val body =
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
                Observable.zip(
                    Observable.just(a), roomRepository.sendProgramacion(body), { _, mensaje ->
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
                    updateEnabledProgramacion(t)
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

    private fun updateEnabledProgramacion(t: Mensaje) {
        roomRepository.updateEnabledProgramacion(t)
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

    fun getProgramacionId(): LiveData<Int> {
        return roomRepository.getProgramacionId()
    }

    fun getProgramacionById(programacionId: Int): LiveData<Programacion> {
        return roomRepository.getProgramacionById(programacionId)
    }

    fun validateProgramacion(p: Programacion) {
        if (p.direccion.isEmpty()) {
            mensajeError.value = "Seleccione Dirección"
            return
        }
        if (p.fechaProgramacion.isEmpty()) {
            mensajeError.value = "Seleccione Fecha Programación"
            return
        }
        if (p.horaProgramacion.isEmpty()) {
            mensajeError.value = "Seleccione Hora Programación"
            return
        }
        p.estadoProgramacion = 23
        p.descripcionEstado = "Programado"
        if (p.descripcionResultado.isNotEmpty()) {
            if (p.fechaReporteProgramacion.isEmpty()) {
                mensajeError.value = "Seleccione Fecha Reporte"
                return
            }
            if (p.horaReporteProgramacion.isEmpty()) {
                mensajeError.value = "Seleccione Hora Reporte"
                return
            }
            p.estadoProgramacion = 24
            p.descripcionEstado = "Ejecutado"
        }
        insertProgramacion(p)
    }

    private fun insertProgramacion(p: Programacion) {
        roomRepository.insertProgramacion(p)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    mensajeSuccess.value = "Actualizado"
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun getVisitas(): LiveData<List<Visita>> {
        return roomRepository.getVisitas()
    }

    fun getDireccionById(medicoId: Int): LiveData<List<MedicoDireccion>> {
        return roomRepository.getDireccionesById(medicoId)
    }

    fun getProgramacionesDetById(programacionId: Int): LiveData<List<ProgramacionDet>> {
        return roomRepository.getProgramacionesDetById(programacionId)
    }

    fun getProgramacionDetById(id: Int): LiveData<ProgramacionDet> {
        return roomRepository.getProgramacionDetById(id)
    }

    fun getStocks(): LiveData<List<Stock>> {
        return roomRepository.getStocks()
    }

    fun validateProgramacionDet(p: ProgramacionDet) {

        if (p.cantidad > p.stock) {
            mensajeError.value = "Cantidad entregada no debe ser mayor a stock"
            return
        }

        if (p.ordenProgramacion == 0) {
            mensajeError.value = "Cantidad entregada no debe ser mayor a stock"
            return
        }

        insertProgramacionDet(p)
    }

    private fun insertProgramacionDet(p: ProgramacionDet) {
        roomRepository.insertProgramacionDet(p)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    mensajeProducto.value = "Guardado"
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }

    fun closeProgramacion(programacionId: Int) {
        roomRepository.closeProgramacion(programacionId)
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

    fun deleteProgramacionDet(p: ProgramacionDet) {
        roomRepository.deleteProgramacionDet(p)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    mensajeSuccess.value = "Eliminado"
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }
            })
    }
}