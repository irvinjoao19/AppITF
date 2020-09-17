package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class Usuario {

    @PrimaryKey
    var usuarioId: Int = 0
    var nroDoc: String = ""
    var email: String = ""
    var perfilId: Int = 0
    var apellidoP: String = ""
    var apellidoM: String = ""
    var nombre: String = ""
    var celular: String = ""
    var fechaNacimiento: String = ""
    var sexo: String = ""
    var supervisorId: Int = 0
    var esSupervisorId: Int = 0
    var estado: Int = 0
    var foto: String = ""
    var login: String = ""
    var pass: String = ""


//    @Ignore
//    var accesos: List<Accesos> = ArrayList()
}