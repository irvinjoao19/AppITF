package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class Medico {

    @PrimaryKey(autoGenerate = true)
    var medicoId: Int = 0
    var medicoSolId: Int = 0
    var identificadorId: Int = 0
    var cpmMedico: String = ""
    var nombreMedico: String = ""
    var apellidoP: String = ""
    var apellidoM: String = ""
    var categoriaId: Int = 0
    var especialidadId: Int = 0
    var especialidadId2: Int = 0
    var email: String = ""
    var fechaNacimiento: String = ""
    var sexo: String = ""
    var telefono: String = ""
    var estado: Int = 0
    var usuarioId: Int = 0

    var nombreIdentificador: String = ""
    var nombreCategoria: String = ""
    var nombreEspecialidad: String = ""
    var nombreEspecialidad2: String = ""
    var identity: Int = 0
    var isSelected: Boolean = false
    var visitadoPor: String = ""
    var direccion: String = ""
    var nombreCompleto: String = ""

    var active: Int = 0 // 1 -> para guardar , 2 -> por completar

    @Ignore
    var direcciones: List<MedicoDireccion>? = null
}