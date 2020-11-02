package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class NuevaDireccion {
    @PrimaryKey(autoGenerate = true)
    var solDireccionId: Int = 0
    var medicoId: Int = 0
    var nombreMedico: String = ""
    var medicoDireccionId: Int = 0
    var solicitante: String = ""
    var fechaSolicitud: String = ""
    var fechaRespuesta: String = ""
    var comentarioRespuesta: String = ""
    var aprobador: String = ""
    var estadoId: Int = 0
    var descripcionEstado: String = ""
    var codigoDepartamento: String = ""
    var nombreDepartamento: String = ""
    var codigoProvincia: String = ""
    var nombreProvincia: String = ""
    var codigoDistrito: String = ""
    var nombreDistrito: String = ""
    var nombreDireccion: String = ""
    var referencia: String = ""
    var nombreInstitucion: String = ""
    var observacion: String = ""

    var identity: Int = 0
    var usuarioId: Int = 0
    var fechaInicio: String = ""
    var fechaFinal: String = ""
    var tipo: Int = 0
    var active: Int = 0
}