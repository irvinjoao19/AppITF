package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Distrito {
    @PrimaryKey
    var codigoDistrito: String = ""
    var nombre: String = ""
}
