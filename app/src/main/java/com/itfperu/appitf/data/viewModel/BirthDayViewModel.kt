package com.itfperu.appitf.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
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

class BirthDayViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) :
    ViewModel() {

    val mensajeError = MutableLiveData<String>()
    val mensajeSuccess = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    val search: MutableLiveData<String> = MutableLiveData()


    fun setError(s: String) {
        mensajeError.value = s
    }

    fun setLoading(s: Boolean) {
        loading.value = s
    }

    fun syncBirthDay(u: Int, m: Int) {
        roomRepository.syncBirthDay(u,m)
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<BirthDay>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: List<BirthDay>) {
                    insertBirthDays(t)
                }

                override fun onError(t: Throwable) {
                    if (t is HttpException) {
                        val body = t.response().errorBody()
                        try {
                            val error = retrofit.errorConverter.convert(body!!)
                            mensajeError.postValue(error!!.Message)
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

    private fun insertBirthDays(p: List<BirthDay>) {
        roomRepository.insertBirthDays(p)
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

    fun getBirthDays(): LiveData<List<BirthDay>> {
        return Transformations.switchMap(search) { input ->
            val f = Gson().fromJson(input, Filtro::class.java)
            roomRepository.getBirthDays(f.usuarioId, f.cicloId)
        }
    }



//    fun sendBirthDay(tipo: Int) {
//        val ots: Observable<List<BirthDay>> = roomRepository.getBirthDayTask(tipo)
//        ots.flatMap { observable ->
//            Observable.fromIterable(observable).flatMap { a ->
//                val json = Gson().toJson(a)
//                Log.i("TAG", json)
//                val body =
//                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
//                Observable.zip(
//                    Observable.just(a), roomRepository.sendBirthDay(body), { _, mensaje ->
//                        mensaje
//                    })
//            }
//        }.subscribeOn(Schedulers.io())
//            .delay(1000, TimeUnit.MILLISECONDS)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : Observer<Mensaje> {
//                override fun onComplete() {
//                    mensajeSuccess.value = "Enviado"
//                }
//
//                override fun onSubscribe(d: Disposable) {}
//                override fun onNext(t: Mensaje) {
//                    updateEnabledBirthDay(t)
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
//            })
//    }
//
//    private fun updateEnabledBirthDay(t: Mensaje) {
//        roomRepository.updateEnabledBirthDay(t)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : CompletableObserver {
//                override fun onSubscribe(d: Disposable) {}
//                override fun onComplete() {}
//
//                override fun onError(e: Throwable) {
//                    mensajeError.value = e.message
//                }
//            })
//    }
}