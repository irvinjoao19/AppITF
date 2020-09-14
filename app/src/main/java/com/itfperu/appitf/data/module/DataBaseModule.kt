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
}