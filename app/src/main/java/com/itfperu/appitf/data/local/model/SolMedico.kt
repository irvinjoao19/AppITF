package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class SolMedico {

    @PrimaryKey(autoGenerate = true)
    var solMedicoId: Int = 0
    var mensajeSol: String = ""
    var usuario: String = ""
    var fecha: String = ""
    var usuarioAprobador: String = ""
    var estadoSol: Int = 0
    var descripcionEstado: String = ""
    var usuarioId: Int = 0
    var identity: Int = 0
    var respuestaAprobador: String = ""
    var tipo: Int = 0 // 1 -> actividades , 2 -> aprobadas

    var estado: Int = 0 // 1 -> por enviar  , 2 -> enviado
    var fechaInicio: String = ""
    var fechaFinal: String = ""

    @Ignore
    var medicos: List<Medico>? = null
}