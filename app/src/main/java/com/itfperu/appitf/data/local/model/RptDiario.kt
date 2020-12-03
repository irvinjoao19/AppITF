package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class RptDiario {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var nombreCiclo : String = ""
    var fechaInicioCiclo: String = ""
    var fechaFinCiclo: String = ""
    var fechaActual: String = ""
    var diasCicloMes: Int = 0
    var diasFecha: Int = 0
    var usuarioId: Int = 0
    var representanteMedico: String = ""
    var cuota: String = ""
    var frecuencia: Int = 0
    var cobertura: Int = 0
}