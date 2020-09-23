package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Ciclo {
    @PrimaryKey
    var cicloId: Int = 0
    var nombre: String = ""
    var desde: String = ""
    var hasta: String = ""
    var nombreEstado: String = ""
    var estado: Int = 0    // 3	-> Activo    4	-> En Proceso     5	-> Cerrado
    var usuarioId: Int = 0
}