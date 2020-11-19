package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class PuntoContacto {
    @PrimaryKey(autoGenerate = true)
    var puntoContactoId: Int = 0
    var usuarioId: Int = 0
    var fechaPuntoContacto: String = ""
    var descripcion: String = ""
    var estadoId: Int = 0
    var descripcionEstado: String = ""
    var latitud: String = ""
    var longitud: String = ""

    var active: Int = 0 // 1 -> para enviar -> 0 enviado
}