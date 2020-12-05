package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class MedicoDireccion {

    @PrimaryKey(autoGenerate = true)
    var medicoDireccionId: Int = 0
    var medicoId: Int = 0
    var codigoDepartamento: String = ""
    var codigoProvincia: String = ""
    var codigoDistrito: String = ""
    var direccion: String = ""
    var referencia: String = ""
    var estado: Int = 0
    var usuarioId: Int = 0
    var institucion : String = ""
    var nombreDepartamento: String = ""
    var nombreProvincia: String = ""
    var nombreDistrito: String = ""

    var identity: Int = 0
    var identityDetalle:Int = 0
    var active : Int = 0 // 1 -> por enviar 0-> enviado
}