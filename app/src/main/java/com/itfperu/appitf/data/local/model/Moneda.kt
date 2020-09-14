package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Moneda {
    @PrimaryKey
    var monedaId: Int = 0
    var codigo: String = ""
    var descripcion: String = ""
    var simbolo: String = ""
    var estado: String = ""
    var estadoId : Int = 0
    var usuarioId : Int = 0
}