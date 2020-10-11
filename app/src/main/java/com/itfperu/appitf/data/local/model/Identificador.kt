package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Identificador {

    @PrimaryKey
    var identificadorId: Int = 0
    var descripcion: String = ""
}