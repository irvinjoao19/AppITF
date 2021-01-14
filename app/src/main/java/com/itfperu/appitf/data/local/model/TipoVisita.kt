package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class TipoVisita {

    @PrimaryKey
    var tipoVisitaId: Int = 0
    var descripcion: String = ""
}