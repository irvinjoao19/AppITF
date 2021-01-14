package com.itfperu.appitf.data.local.model

open class Sync {
    var ciclos: List<Ciclo> = ArrayList()
    var estados: List<Estado> = ArrayList()
    var duracions: List<Duracion> = ArrayList()
    var personals: List<Personal> = ArrayList()

    var categorias: List<Categoria> = ArrayList()
    var identificadors: List<Identificador> = ArrayList()
    var especialidads: List<Especialidad> = ArrayList()
    var ubigeos: List<Ubigeo> = ArrayList()
    var medicos: List<Medico> = ArrayList()
    var visitas: List<Visita> = ArrayList()
    var tipoVisita : List<TipoVisita> = ArrayList()
}