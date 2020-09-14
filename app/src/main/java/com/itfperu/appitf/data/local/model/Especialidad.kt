package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Especialidad {
    @PrimaryKey
    var especialidadId: Int = 0
    var codigo: String = ""
    var descripcion: String = ""
    var estado: String = ""
    var estadoId: Int = 0
    var usuarioId: Int = 0
}