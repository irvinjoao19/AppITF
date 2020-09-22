package com.itfperu.appitf.data.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itfperu.appitf.data.viewModel.*
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
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}