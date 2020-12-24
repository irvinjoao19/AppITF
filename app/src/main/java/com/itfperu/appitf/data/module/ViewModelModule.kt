package com.itfperu.appitf.data.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.Worker
import com.itfperu.appitf.data.viewModel.*
import com.itfperu.appitf.ui.workManager.MedicoWork
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(UsuarioViewModel::class)
    internal abstract fun bindUserViewModel(usuarioViewModel: UsuarioViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoriaViewModel::class)
    internal abstract fun bindCategoriaViewModel(categoriaViewModel: CategoriaViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EspecialidadViewModel::class)
    internal abstract fun bindEspecialidadViewModel(especialidadViewModel: EspecialidadViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FeriadoViewModel::class)
    internal abstract fun bindFeriadoViewModel(feriadoViewModel: FeriadoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PerfilViewModel::class)
    internal abstract fun bindPerfilViewModel(perfilViewModel: PerfilViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MonedaViewModel::class)
    internal abstract fun bindMonedaViewModel(monedaViewModel: MonedaViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductoViewModel::class)
    internal abstract fun bindProductoViewModel(productoViewModel: ProductoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TipoProductoViewModel::class)
    internal abstract fun bindTipoProductoViewModel(tipoProductoViewModel: TipoProductoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VisitaViewModel::class)
    internal abstract fun bindVisitaViewModel(visitaViewModel: VisitaViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PersonalViewModel::class)
    internal abstract fun bindPersonalViewModel(personalViewModel: PersonalViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CicloViewModel::class)
    internal abstract fun bindCicloViewModel(cicloViewModel: CicloViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ActividadViewModel::class)
    internal abstract fun bindActividadViewModel(actividadViewModel: ActividadViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MedicoViewModel::class)
    internal abstract fun bindMedicoViewModel(medicoViewModel: MedicoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TargetViewModel::class)
    internal abstract fun bindTargetViewModel(targetViewModel: TargetViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProgramacionViewModel::class)
    internal abstract fun bindProgramacionViewModel(programacionViewModel: ProgramacionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DireccionViewModel::class)
    internal abstract fun bindDireccionViewModel(direccionViewModel: DireccionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PuntoContactoViewModel::class)
    internal abstract fun bindPuntoContactoViewModel(puntoContactoViewModel: PuntoContactoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StockViewModel::class)
    internal abstract fun bindStockViewModel(stockViewModel: StockViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReporteViewModel::class)
    internal abstract fun bindReporteViewModel(reporteViewModel: ReporteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BirthDayViewModel::class)
    internal abstract fun bindBirthDayViewModel(birthDayViewModel: BirthDayViewModel): ViewModel


    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}