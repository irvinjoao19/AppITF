package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Estado {
    @PrimaryKey
    var estadoId: Int = 0
    var grupo: String = ""
    var descripcion: String = ""
}