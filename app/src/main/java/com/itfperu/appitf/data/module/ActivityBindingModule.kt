package com.itfperu.appitf.data.module

import com.itfperu.appitf.ui.activities.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    internal abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [FragmentBindingModule.Main::class])
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [FragmentBindingModule.Form::class])
    internal abstract fun bindFormActivity(): FormActivity

    @ContributesAndroidInjector
    internal abstract fun bindPerfilActivity(): PerfilActivity

    @ContributesAndroidInjector
    internal abstract fun bindPreviewCameraActivity(): PreviewCameraActivity
}