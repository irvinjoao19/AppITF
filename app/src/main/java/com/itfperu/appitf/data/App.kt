package com.itfperu.appitf.data

import androidx.work.*
import com.itfperu.appitf.data.components.DaggerApplicationComponent
import com.itfperu.appitf.ui.workManager.WorkManagerFactory
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject

class App : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        configureWorkManager()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent
            .builder()
            .application(this)
            .build()
    }

    @Inject
    lateinit var daggerAwareWorkerFactory: WorkManagerFactory

    private fun configureWorkManager() {
        val config = Configuration.Builder()
            .setWorkerFactory(daggerAwareWorkerFactory)
            .build()
        WorkManager.initialize(this, config)
    }
}