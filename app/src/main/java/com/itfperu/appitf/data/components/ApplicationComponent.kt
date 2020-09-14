package com.itfperu.appitf.data.components

import android.app.Application
import com.itfperu.appitf.data.App
import com.itfperu.appitf.data.module.ActivityBindingModule
import com.itfperu.appitf.data.module.DataBaseModule
import com.itfperu.appitf.data.module.RetrofitModule
import com.itfperu.appitf.data.module.ServicesModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBindingModule::class,
        RetrofitModule::class,
        DataBaseModule::class,
        ServicesModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }
}