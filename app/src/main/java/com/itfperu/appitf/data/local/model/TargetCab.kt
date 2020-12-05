package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class TargetCab {
    @PrimaryKey
    var targetCabId: Int = 0
    var fechaSolicitud: String = ""
    var aprobador: String = ""
    var estado: Int = 0 // colocar 16
    var nombreEstado: String = ""
    var usuarioSolicitante: String = ""
    var usuarioId: Int = 0
    var tipoTarget: String = "" // A -> Alta , B -> baja
    var tipo: Int = 0 // 1 -> altas ,2 -> aprobaciones

    var active: Int = 0 // 1 -> por enviar  , 2 -> enviado
    var identity: Int = 0
    var fechaInicio: String = ""
    var fechaFinal: String = ""

    var cmpMedico: String = ""
    var nombresMedico: String = ""
    var descripcionCategoria: String = ""
    var descripcionEspecialidad: String = ""
    var numeroContactos: Int = 0

    @Ignore
    var detalle: List<TargetDet>? = null
}