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
    @ViewModelKey(ITFViewModel::class)
    internal abstract fun bindITFViewModel(itfViewModel: ITFViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}