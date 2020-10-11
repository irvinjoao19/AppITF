package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Departamento {

    @PrimaryKey
    var codigoDepartamento: String = ""
    var nombre: String = ""

}
