package com.itfperu.appitf.data.local.model

open class Filtro(var login: String, var pass: String, var imei: String, var version: String) {

    var pageIndex: Int = 0
    var pageSize: Int = 0
    var search: String = ""

}