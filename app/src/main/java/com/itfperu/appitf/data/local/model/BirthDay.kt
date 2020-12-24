package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class BirthDay {
    @PrimaryKey(autoGenerate = true)
    var targetId: Int = 0
    var usuarioId: Int = 0
    var nombreMedico: String = ""
    var cmpMedico: String = ""
    var medicoId: Int = 0
    var categoriaId: Int = 0
    var descripcionCategoria: String = ""
    var especialidad: Int = 0
    var descripcionEspecialidad: String = ""
    var numeroContacto: Int = 0
    var nombreEstado: String = ""
    var fechaNacimiento: String = ""
    var mesId: Int = 0
}