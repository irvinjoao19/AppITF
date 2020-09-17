package com.itfperu.appitf.data.module

import com.itfperu.appitf.ui.fragments.*
import com.itfperu.appitf.ui.fragments.edition.*
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
        internal abstract fun providCategoriaFragment(): CategoriaFragment

        @ContributesAndroidInjector
        internal abstract fun providEspecialidadFragment(): EspecialidadFragment

        @ContributesAndroidInjector
        internal abstract fun providFeriadoFragment(): FeriadoFragment

        @ContributesAndroidInjector
        internal abstract fun providMonedaFragment(): MonedaFragment

        @ContributesAndroidInjector
        internal abstract fun providProductoFragment(): ProductoFragment

        @ContributesAndroidInjector
        internal abstract fun providTipoProductoFragment(): TipoProductoFragment

        @ContributesAndroidInjector
        internal abstract fun providVisitaFragment(): VisitaFragment
    }

    @Module
    abstract class Form {
        @ContributesAndroidInjector
        internal abstract fun providEditCategoriaFragment(): EditCategoriaFragment

        @ContributesAndroidInjector
        internal abstract fun providEspecialidadFragment(): EditEspecialidadFragment

        @ContributesAndroidInjector
        internal abstract fun providEditFeriadoFragment(): EditFeriadoFragment

        @ContributesAndroidInjector
        internal abstract fun providEditMonedaFragment(): EditMonedaFragment

        @ContributesAndroidInjector
        internal abstract fun providEditPerfilFragment(): EditPerfilFragment

        @ContributesAndroidInjector
        internal abstract fun providEditProductoFragment(): ProductoFragment

        @ContributesAndroidInjector
        internal abstract fun providEditTipoProductoFragment(): TipoProductoFragment

        @ContributesAndroidInjector
        internal abstract fun providEditVisitaFragment(): EditVisitaFragment

    }
}