package com.itfperu.appitf.data.module

import android.app.Application
import androidx.room.Room
import com.itfperu.appitf.data.local.AppDataBase
import com.itfperu.appitf.data.local.dao.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataBaseModule {

    @Provides
    @Singleton
    internal fun provideRoomDatabase(application: Application): AppDataBase {
        if (AppDataBase.INSTANCE == null) {
            synchronized(AppDataBase::class.java) {
                if (AppDataBase.INSTANCE == null) {
                    AppDataBase.INSTANCE = Room.databaseBuilder(
                        application.applicationContext,
                        AppDataBase::class.java, AppDataBase.DB_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
        }
        return AppDataBase.INSTANCE!!
    }

    @Provides
    internal fun provideUsuarioDao(appDataBase: AppDataBase): UsuarioDao {
        return appDataBase.usuarioDao()
    }

    @Provides
    internal fun provideAccesosDao(appDataBase: AppDataBase): AccesosDao {
        return appDataBase.accesosDao()
    }

    @Provides
    internal fun providePerfilDao(appDataBase: AppDataBase): PerfilDao {
        return appDataBase.perfilDao()
    }

    @Provides
    internal fun provideMonedaDao(appDataBase: AppDataBase): MonedaDao {
        return appDataBase.monedaDao()
    }

    @Provides
    internal fun provideFeriadoDao(appDataBase: AppDataBase): FeriadoDao {
        return appDataBase.feriadoDao()
    }

    @Provides
    internal fun provideCategoriDao(appDataBase: AppDataBase): CategoriaDao {
        return appDataBase.categoriaDao()
    }

    @Provides
    internal fun provideEspecialidadDao(appDataBase: AppDataBase): EspecialidadDao {
        return appDataBase.especialidadDao()
    }

    @Provides
    internal fun provideProductoDao(appDataBase: AppDataBase): ProductoDao {
        return appDataBase.productoDao()
    }

    @Provides
    internal fun provideTipoProductoDao(appDataBase: AppDataBase): TipoProductoDao {
        return appDataBase.tipoProductoDao()
    }

    @Provides
    internal fun provideVisitaDao(appDataBase: AppDataBase): VisitaDao {
        return appDataBase.visitaDao()
    }

    @Provides
    internal fun provideControlDao(appDataBase: AppDataBase): ControlDao {
        return appDataBase.controlDao()
    }

    @Provides
    internal fun providePersonalDao(appDataBase: AppDataBase): PersonalDao {
        return appDataBase.personalDao()
    }

    @Provides
    internal fun provideCicloDao(appDataBase: AppDataBase): CicloDao {
        return appDataBase.cicloDao()
    }

    @Provides
    internal fun provideActividadDao(appDataBase: AppDataBase): ActividadDao {
        return appDataBase.actividadDao()
    }

    @Provides
    internal fun provideEstadoDao(appDataBase: AppDataBase): EstadoDao {
        return appDataBase.estadoDao()
    }

    @Provides
    internal fun provideDuracionDao(appDataBase: AppDataBase): DuracionDao {
        return appDataBase.duracionDao()
    }

    @Provides
    internal fun provideSolMedicoDao(appDataBase: AppDataBase): SolMedicoDao {
        return appDataBase.solMedicoDao()
    }

    @Provides
    internal fun provideMedicoDao(appDataBase: AppDataBase): MedicoDao {
        return appDataBase.medicoDao()
    }

    @Provides
    internal fun provideMedicoDireccionDao(appDataBase: AppDataBase): MedicoDireccionDao {
        return appDataBase.medicoDireccionDao()
    }

    @Provides
    internal fun provideIdentificadorDao(appDataBase: AppDataBase): IdentificadorDao {
        return appDataBase.identificadorDao()
    }

    @Provides
    internal fun provideTargetDao(appDataBase: AppDataBase): TargetDao {
        return appDataBase.targetDao()
    }

    @Provides
    internal fun provideTargetCabDao(appDataBase: AppDataBase): TargetCabDao {
        return appDataBase.targetCabDao()
    }

    @Provides
    internal fun provideTargetDetDao(appDataBase: AppDataBase): TargetDetDao {
        return appDataBase.targetDetDao()
    }

    @Provides
    internal fun provideTargetInfoDao(appDataBase: AppDataBase): TargetInfoDao {
        return appDataBase.targetInfoDao()
    }

    @Provides
    internal fun provideUbigeoDao(appDataBase: AppDataBase): UbigeoDao {
        return appDataBase.ubigeoDao()
    }
}