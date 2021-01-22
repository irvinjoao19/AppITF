package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class ProgramacionDet {

    @PrimaryKey(autoGenerate = true)
    var programacionDetId: Int = 0
    var programacionId: Int = 0
    var productoId: Int = 0
    var ordenProgramacion: Int = 0
    var codigoProducto: String = ""
    var descripcionProducto: String = ""
    var lote: String = ""
    var cantidad: Int = 0
    var stock: Int = 0
    var identity: Int = 0
    var active: Int = 0 // 1 -> para enviar 0 -> enviado o del servidor
}