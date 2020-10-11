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
        Feriado::class,
        Categoria::class,
        Especialidad::class,
        Producto::class,
        TipoProducto::class,
        Visita::class,
        Control::class,
        Personal::class,
        Ciclo::class,
        Actividad::class,
        Estado::class,
        Duracion::class,
        SolMedico::class,
        Medico::class,
        MedicoDireccion::class,
        Identificador::class,
        Departamento::class,
        Provincia::class,
        Distrito::class,
        TargetM::class,
        TargetCab::class,
        TargetDet::class
    ],
    views = [Nombre::class],
    version = 44, // version 1 en play store
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun accesosDao(): AccesosDao
    abstract fun perfilDao(): PerfilDao
    abstract fun monedaDao(): MonedaDao
    abstract fun feriadoDao(): FeriadoDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun especialidadDao(): EspecialidadDao
    abstract fun productoDao(): ProductoDao
    abstract fun tipoProductoDao(): TipoProductoDao
    abstract fun visitaDao(): VisitaDao
    abstract fun controlDao(): ControlDao
    abstract fun personalDao(): PersonalDao
    abstract fun cicloDao(): CicloDao
    abstract fun actividadDao(): ActividadDao
    abstract fun estadoDao(): EstadoDao
    abstract fun duracionDao(): DuracionDao
    abstract fun solMedicoDao(): SolMedicoDao

    abstract fun medicoDao(): MedicoDao
    abstract fun medicoDireccionDao(): MedicoDireccionDao
    abstract fun identificadorDao(): IdentificadorDao
    abstract fun departamentoDao(): DepartamentoDao
    abstract fun provinciaDao(): ProvinciaDao
    abstract fun distritoDao(): DistritoDao
    abstract fun targetDao(): TargetDao
    abstract fun targetCabDao(): TargetCabDao
    abstract fun targetDetDao(): TargetDetDao

    companion object {
        @Volatile
        var INSTANCE: AppDataBase? = null
        val DB_NAME = "itf_db"
    }

    fun getDatabase(context: Context): AppDataBase {
        if (INSTANCE == null) {
            synchronized(AppDataBase::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java, "itf_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
        }
        return INSTANCE!!
    }
}