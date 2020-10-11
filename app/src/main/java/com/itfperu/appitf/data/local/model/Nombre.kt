package com.itfperu.appitf.data.local.model

import androidx.room.DatabaseView

@DatabaseView("SELECT (nombre || ' ' || apellidoP || ' ' || apellidoM) as nombreCompleto FROM Usuario ")
open class Nombre {
    var nombreCompleto: String = ""
}