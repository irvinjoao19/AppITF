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

    @ContributesAndroidInjector(modules = [FragmentBindingModule.Medico::class])
    internal abstract fun bindMedicoActivity(): MedicoActivity

    @ContributesAndroidInjector
    internal abstract fun bindTargetActivity(): TargetActivity

    @ContributesAndroidInjector
    internal abstract fun bindSearchMedicoActivity(): SearchMedicoActivity

    @ContributesAndroidInjector(modules = [FragmentBindingModule.Visita::class])
    internal abstract fun bindVisitaActivity(): VisitaActivity
}