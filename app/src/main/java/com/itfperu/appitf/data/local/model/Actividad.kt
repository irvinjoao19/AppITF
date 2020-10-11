package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Actividad {

    @PrimaryKey(autoGenerate = true)
    var actividadId: Int = 0
    var cicloId: Int = 0
    var fechaActividad: String = ""
    var fecha: String = ""
    var duracionId: Int = 0
    var descripcionDuracion: String = ""
    var detalle: String = ""
    var estado: Int = 0
    var descripcionEstado: String = ""
    var aprobador: String = ""
    var observacion: String = ""
    var usuario: String = ""
    var usuarioId: Int = 0
    var identity: Int = 0
    var fechaRespuesta: String = ""
    var tipoInterfaz: String = ""

    var nombreCiclo: String = ""
    var active: Int = 0 // 1 -> para enviar , 2 -> enviado
    var tipo: Int = 0 // 1 -> actividades, 2 -> aprobaciones
}