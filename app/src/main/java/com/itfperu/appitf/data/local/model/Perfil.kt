package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Perfil {
    @PrimaryKey
    var perfilId: Int = 0
    var codigo: String = ""
    var descripcion: String = ""
    var estado: String = ""
    var estadoId : Int = 0
    var usuarioId : Int = 0
    var cuotaFrecuencia : Int = 0
    var cuotaCobertura : Int = 0
}