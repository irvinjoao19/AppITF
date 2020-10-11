package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Provincia {
    @PrimaryKey
    var codigoProvincia: String = ""
    var nombre: String = ""
}
