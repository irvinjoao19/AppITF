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

        @ContributesAndroidInjector
        internal abstract fun providPersonakFragment(): PersonalFragment

        @ContributesAndroidInjector
        internal abstract fun providCicloFragment(): CicloFragment

        @ContributesAndroidInjector
        internal abstract fun providActividadFragment(): ActividadFragment

        @ContributesAndroidInjector
        internal abstract fun providMedicoFragment(): MedicosFragment

        @ContributesAndroidInjector
        internal abstract fun providTargetFragment(): TargetFragment

        @ContributesAndroidInjector
        internal abstract fun providTargetAltasFragment(): TargetAltasFragment

        @ContributesAndroidInjector
        internal abstract fun providProgramacionFragment(): ProgramacionFragment
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
        internal abstract fun providEditProductoFragment(): EditProductoFragment

        @ContributesAndroidInjector
        internal abstract fun providEditTipoProductoFragment(): EditTipoProductoFragment

        @ContributesAndroidInjector
        internal abstract fun providEditVisitaFragment(): EditVisitaFragment

        @ContributesAndroidInjector
        internal abstract fun providEditPersonalFragment(): EditPersonalFragment

        @ContributesAndroidInjector
        internal abstract fun providEditUsuarioFragment(): EditUsuarioFragment

        @ContributesAndroidInjector
        internal abstract fun providEditCicloFragment(): EditCicloFragment

        @ContributesAndroidInjector
        internal abstract fun providEditActividadesFragment(): EditActividadesFragment

        @ContributesAndroidInjector
        internal abstract fun providEditAprobationFragment(): EditAprobationAFragment

        @ContributesAndroidInjector
        internal abstract fun providEditSolMedicoFragment(): EditSolMedicoFragment

        @ContributesAndroidInjector
        internal abstract fun providEditTargetFragment(): EditTargetFragment
    }

    @Module
    abstract class Medico {
        @ContributesAndroidInjector
        internal abstract fun providGeneralFragment(): GeneralFragment

        @ContributesAndroidInjector
        internal abstract fun providDireccionFragment(): DireccionFragment
    }

    @Module
    abstract class Visita {
        @ContributesAndroidInjector
        internal abstract fun providVisitaGeneralFragment(): VisitaGeneralFragment

        @ContributesAndroidInjector
        internal abstract fun providVisitaProductoFragment(): VisitaProductoFragment
    }
}