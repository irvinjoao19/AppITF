package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Accesos {
    @PrimaryKey(autoGenerate = true)
    var opcionId: Int = 0
    var nombre: String = ""
    var tipo: Int = 0
    var usuarioId: Int = 0
}