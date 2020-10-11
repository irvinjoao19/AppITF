package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class TargetM {
    @PrimaryKey
    var targetId: Int = 0
    var usuarioId: Int = 0
    var nombreUsuario: String = ""
    var cmpMedico: String = ""
    var medicoId: Int = 0
    var nombreMedico: String = ""
    var categoriaId: Int = 0
    var descripcionCategoria: String = ""
    var especialidadId: Int = 0
    var descripcionEspecialidad: String = ""
    var numeroContactos: Int = 0
    var estado: String = ""
}