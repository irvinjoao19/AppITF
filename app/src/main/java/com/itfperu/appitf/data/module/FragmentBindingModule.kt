package com.itfperu.appitf.data.module

import com.itfperu.appitf.ui.fragments.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

abstract class FragmentBindingModule {

    @Module
    abstract class Main {
        @ContributesAndroidInjector
        internal abstract fun providMainFragment(): MainFragment

        @ContributesAndroidInjector
        internal abstract fun providPerfilFragment(): PerfilFragment

        @ContributesAndroidInjector
        internal abstract fun providMonedaFragment(): MonedaFragment
    }
}