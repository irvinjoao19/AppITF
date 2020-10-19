package com.itfperu.appitf.data.local.model

open class Filtro {

    var categorias: Int = 0
    var especialidad: Int = 0
    var login: String = ""
    var pass: String = ""
    var imei: String = ""
    var version: String = ""

    var pageIndex: Int = 0
    var pageSize: Int = 0
    var search: String = ""

    var usuarioId: Int = 0
    var cicloId: Int = 0
    var estadoId: Int = 0
    var tipo: Int = 0
    var tipoTarget: String = ""

    var finicio: String = ""
    var ffinal: String = ""

    constructor()

    constructor(login: String, pass: String, imei: String, version: String) {
        this.login = login
        this.pass = pass
        this.imei = imei
        this.version = version
    }

    // actividades
    constructor(usuarioId: Int, cicloId: Int, estadoId: Int, tipo: Int) {
        this.usuarioId = usuarioId
        this.cicloId = cicloId
        this.estadoId = estadoId
        this.tipo = tipo
    }

    // medicos
    constructor(
        usuarioId: Int, finicio: String, ffinal: String, estadoId: Int, tipo: Int
    ) {
        this.usuarioId = usuarioId
        this.finicio = finicio
        this.ffinal = ffinal
        this.estadoId = estadoId
        this.tipo = tipo
    }
    // targets
    constructor(
        usuarioId: Int, finicio: String, ffinal: String, estadoId: Int, tipo: Int, tipoTarget: String
    ) {
        this.usuarioId = usuarioId
        this.finicio = finicio
        this.ffinal = ffinal
        this.estadoId = estadoId
        this.tipo = tipo
        this.tipoTarget = tipoTarget
    }
}