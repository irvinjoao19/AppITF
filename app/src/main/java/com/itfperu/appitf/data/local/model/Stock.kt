package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Stock {
    @PrimaryKey
    var productoId: Int = 0
    var codigoProducto: String = ""
    var descripcion: String = ""
    var lote: String = ""
    var stock: Double = 0.0
}