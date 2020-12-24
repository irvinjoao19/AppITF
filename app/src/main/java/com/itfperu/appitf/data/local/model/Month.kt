package com.itfperu.appitf.data.local.model

open class Month {
    var codigo: Int = 0
    var nombre: String = ""

    constructor()

    constructor(codigo: Int, nombre: String) {
        this.codigo = codigo
        this.nombre = nombre
    }
}