package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Duracion {
    @PrimaryKey
    var duracionId: Int = 0
    var descripcion: String = ""
}