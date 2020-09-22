package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Producto {
    @PrimaryKey
    var productoId: Int = 0
    var codigo: String = ""
    var descripcion: String = ""
    var tipoProductoId: Int = 0
    var abreviatura: String = ""
    var tipo: String = ""
    var control: String = ""
    var controlId: Int = 0
    var estado: String = ""
    var estadoId: Int = 0
    var usuarioId: Int = 0
    var descripcionTipoProducto: String  = ""
}