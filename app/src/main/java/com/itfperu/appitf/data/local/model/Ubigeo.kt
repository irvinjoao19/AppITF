package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Ubigeo {
    @PrimaryKey
    var id: Int = 0
    var codDepartamento: String = ""
    var nombreDepartamento: String = ""
    var codProvincia: String = ""
    var provincia: String = ""
    var codDistrito: String = ""
    var nombreDistrito: String = ""
}