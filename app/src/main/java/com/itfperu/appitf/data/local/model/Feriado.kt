package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Feriado {
    @PrimaryKey
    var feriadoId: Int = 0
    var fecha: String = ""
    var descripcion: String = ""
    var estado: String = ""
    var estadoId : Int = 0
    var usuarioId : Int = 0
}