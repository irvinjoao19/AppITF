package com.itfperu.appitf.data.module

import com.itfperu.appitf.ui.workManager.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(MedicoWork::class)
    internal abstract fun bindMedicoWork(medicoWork: MedicoWork.Factory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(DireccionWork::class)
    internal abstract fun bindDireccionWork(direccionWork: DireccionWork.Factory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(TargetWork::class)
    internal abstract fun bindTargetWork(targetWork: TargetWork.Factory): ChildWorkerFactory
}