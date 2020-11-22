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
        TargetM::class,
        TargetCab::class,
        TargetDet::class,
        TargetInfo::class,
        Ubigeo::class,
        Programacion::class,
        ProgramacionDet::class,
        Stock::class,
        NuevaDireccion::class,
        PuntoContacto::class
    ],
    views = [Nombre::class],
    version = 70, // version 1 en play store
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
    abstract fun targetDao(): TargetDao
    abstract fun targetCabDao(): TargetCabDao
    abstract fun targetDetDao(): TargetDetDao
    abstract fun targetInfoDao(): TargetInfoDao
    abstract fun ubigeoDao(): UbigeoDao
    abstract fun programacionDao(): ProgramacionDao
    abstract fun programacionDetDao(): ProgramacionDetDao
    abstract fun stockDao(): StockDao
    abstract fun nuevaDireccionDao(): NuevaDireccionDao
    abstract fun puntoContactoDao(): PuntoContactoDao

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