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

    constructor()

    constructor(login: String, pass: String, imei: String, version: String) {
        this.login = login
        this.pass = pass
        this.imei = imei
        this.version = version
    }
}