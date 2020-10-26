package com.itfperu.appitf.data.local.model

import androidx.room.Entity
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
    var apellidoP: String = ""
    var apellidoM: String = ""
    var medicoId: Int = 0
    var cmpMedico: String = ""
    var nombreMedico: String = ""
    var apellidoPMedico: String = ""
    var apellidoMMedico: String = ""
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
    var ordenProgramacion: Int = 0
    var codigoProducto: String = ""
    var descripcionProducto: String = ""
    var loteProgramacion: String = ""
    var cantidadProgramacion: Double = 0.0

    var identity : Int = 0
    var active : Int = 0 // 1 -> para enviar 0 -> enviado o del servidor
}