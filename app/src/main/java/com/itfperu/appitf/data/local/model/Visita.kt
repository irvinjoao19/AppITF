package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Visita {
    @PrimaryKey
    var visitaId: Int = 0
    var descripcion: String = ""
    var resultado: String = ""
    var estado: String = ""
    var estadoId: Int = 0
    var usuarioId: Int = 0
}