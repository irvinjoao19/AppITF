package com.itfperu.appitf.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itfperu.appitf.data.local.model.*
import com.itfperu.appitf.data.local.repository.ApiError
import com.itfperu.appitf.data.local.repository.AppRepository
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ReporteViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) :
    ViewModel() {

    val mensajeErrorGeneral = MutableLiveData<String>()
    val mensajeErrorDiario = MutableLiveData<String>()
    val mensajeSuccess = MutableLiveData<String>()
    val mensajeError = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    val search: MutableLiveData<String> = MutableLiveData()
    val rptGeneral: MutableLiveData<List<RptGeneral>> = MutableLiveData()
    val rptDiario: MutableLiveData<List<RptDiario>> = MutableLiveData()


    fun setLoading(s: Boolean) {
        loading.value = s
    }

    fun syncRRMMGeneral(c: Int, u: Int) {
        roomRepository.clearRptGeneral()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    roomRepository.syncRRMMGeneral(c, u)
                        .delay(1000, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<List<RptGeneral>> {
                            override fun onSubscribe(d: Disposable) {}
                            override fun onComplete() {}
                            override fun onNext(t: List<RptGeneral>) {
                                insertRptGeneral(t)
                            }

                            override fun onError(t: Throwable) {
                                loading.value = false
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
            })
    }

    fun syncSUPGeneral(c: Int, u: Int) {
        roomRepository.clearRptGeneral()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    roomRepository.syncSUPGeneral(c, u)
                        .delay(1000, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            object : Observer<List<RptGeneral>> {
                                override fun onSubscribe(d: Disposable) {}
                                override fun onComplete() {}
                                override fun onNext(t: List<RptGeneral>) {
                                    insertRptGeneral(t)
                                }

                                override fun onError(t: Throwable) {
                                    loading.value = false
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
            })
    }

    private fun insertRptGeneral(g: List<RptGeneral>) {
        roomRepository.insertRptGeneral(g)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    loading.value = false
                }
            })
    }

    fun syncRRMMDiario(c: Int, u: Int) {
        roomRepository.clearRptDiario()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    roomRepository.syncRRMMDiario(c, u)
                        .delay(1000, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<List<RptDiario>> {
                            override fun onSubscribe(d: Disposable) {}
                            override fun onComplete() {}
                            override fun onNext(t: List<RptDiario>) {
                                insertRptDiario(t)
                            }

                            override fun onError(t: Throwable) {
                                loading.value = false
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
            })
    }

    fun syncSUPDiario(c: Int, u: Int) {
        roomRepository.clearRptDiario()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    roomRepository.syncSUPDiario(c, u)
                        .delay(1000, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<List<RptDiario>> {
                            override fun onSubscribe(d: Disposable) {}
                            override fun onComplete() {}
                            override fun onNext(t: List<RptDiario>) {
                                insertRptDiario(t)
                            }

                            override fun onError(t: Throwable) {
                                loading.value = false
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
            })
    }

    private fun insertRptDiario(g: List<RptDiario>) {
        roomRepository.insertRptDiario(g)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    loading.value = false
                }
            })
    }

    fun syncCiclo() {
        roomRepository.syncCiclo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Ciclo>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onError(e: Throwable) {

                }

                override fun onNext(t: List<Ciclo>) {
                    insertCiclo(t)
                }
            })

    }

    private fun insertCiclo(c: List<Ciclo>) {
        roomRepository.insertCiclos(c)
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

    fun getCiclos(): LiveData<List<Ciclo>> {
        return roomRepository.getCiclos()
    }

    fun getRptGeneralCabecera(): LiveData<RptGeneral> {
        return roomRepository.getRptGeneralCabecera()
    }

    fun getRptGeneralFecha(): LiveData<List<RptGeneral>> {
        return roomRepository.getRptGeneral()
    }

    fun getRptGeneralMes(): LiveData<List<RptGeneral>> {
        return roomRepository.getRptGeneral()
    }

    fun getRptDiarioCabecera(): LiveData<RptDiario> {
        return roomRepository.getRptDiarioCabecera()
    }

    fun getRptDiario(): LiveData<List<RptDiario>> {
        return roomRepository.getRptDiario()
    }
}