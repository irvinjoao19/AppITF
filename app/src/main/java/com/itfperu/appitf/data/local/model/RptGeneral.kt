package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class RptGeneral {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var usuarioId: Int = 0
    var representanteMedico: String = ""
    var nombreCiclo: String = ""
    var fechaInicioCiclo: String = ""
    var fechaFinCiclo: String = ""
    var fechaActual: String = ""
    var diasCicloMes: Int = 0
    var diasFecha: Int = 0
    var accion: String = ""
    var cuotaMes: Int = 0
    var numeroVisita: Int = 0
    var porcentajeMes: Int = 0
    var saldoMes: Int = 0
    var cuotaFecha: Int = 0
    var porcentajeFecha: Int = 0
    var saldoFecha: Int = 0
}