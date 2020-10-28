package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class Programacion {
    @PrimaryKey(autoGenerate = true)
    var programacionId: Int = 0
    var cicloId: Int = 0
    var nombreCiclo: String = ""
    var numeroVisita: String = ""
    var usuarioId: Int = 0
    var nombreUsuario: String = ""
    var medicoId: Int = 0
    var cmpMedico: String = ""
    var nombreMedico: String = ""
    var categoria: String = ""
    var especialidad: String = ""
    var fechaProgramacion: String = ""
    var horaProgramacion: String = ""
    var fechaReporteProgramacion: String = ""
    var horaReporteProgramacion: String = ""   
    var visitaAcompanada: String = ""
    var dataAcompaniante: String = ""
    var latitud: String = ""
    var longitud: String = ""
    var estadoProgramacion: Int = 0
    var descripcionEstado: String = ""
    var resultadoVisitaId: Int = 0
    var descripcionResultado: String = ""
    var direccionId : Int = 0
    var direccion:String = ""

    var identity : Int = 0
    var active : Int = 0 // 1 -> para enviar 0 -> enviado o del servidor , 2 -> incompleto

    @Ignore
    var productos : List<ProgramacionDet>? = null
}