package com.itfperu.appitf.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.itfperu.appitf.data.local.dao.*
import com.itfperu.appitf.data.local.model.*

@Database(
    entities = [
        Usuario::class,
        Accesos::class,
        Perfil::class,
        Moneda::class,
        Feriado::class
    ],
    version = 3, // version 1 en play store
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun accesosDao(): AccesosDao
    abstract fun perfilDao(): PerfilDao
    abstract fun monedaDao(): MonedaDao
    abstract fun feriadoDao(): FeriadoDao

    companion object {
        @Volatile
        var INSTANCE: AppDataBase? = null
        val DB_NAME = "utf_db"
    }

    fun getDatabase(context: Context): AppDataBase {
        if (INSTANCE == null) {
            synchronized(AppDataBase::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java, "utf_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
        }
        return INSTANCE!!
    }
}