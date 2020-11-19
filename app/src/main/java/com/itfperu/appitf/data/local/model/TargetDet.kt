package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class TargetDet {
    @PrimaryKey(autoGenerate = true)
    var targetDetId: Int = 0
    var targetCabId: Int = 0
    var medicoId: Int = 0
    var cmp: String = ""
    var categoriaId: Int = 0
    var especialidadId: Int = 0
    var nroContacto: Int = 0
    var mensajeRespuesta: String = ""

    var nombreMedico: String = ""
    var nombreCategoria: String = ""
    var nombreEspecialidad: String = ""

    var identity: Int = 0
    var estado: Int = 0 // 16 para enviar
    var tipoTarget: String = "" // A -> ALTAS	B -> BAJAS
    var active: Int = 0 // 1 -> terminado -> 0 -> por completar

    var estadoTarget: Int = 0 // 16 para enviar
    var visitadoPor : String = ""
    var visitado: Int = 0
    var nrovisita: Int = 0
    var mensajeNrovisita: String = ""

    @Ignore
    var infos: List<TargetInfo>? = null
}