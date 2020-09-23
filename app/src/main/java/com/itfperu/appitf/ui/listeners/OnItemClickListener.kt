package com.itfperu.appitf.ui.listeners

import android.view.View
import com.itfperu.appitf.data.local.model.*

interface OnItemClickListener {

    interface PerfilListener {
        fun onItemClick(p: Perfil, view: View, position: Int)
    }

    interface MonedaListener {
        fun onItemClick(m: Moneda, view: View, position: Int)
    }

    interface FeriadoListener {
        fun onItemClick(f: Feriado, view: View, position: Int)
    }

    interface CategoriaListener {
        fun onItemClick(c: Categoria, view: View, position: Int)
    }

    interface EspecialidadListener {
        fun onItemClick(e: Especialidad, view: View, position: Int)
    }

    interface ProductoListener {
        fun onItemClick(p: Producto, view: View, position: Int)
    }

    interface TipoProductoListener {
        fun onItemClick(t: TipoProducto, view: View, position: Int)
    }

    interface VisitaListener {
        fun onItemClick(v: Visita, view: View, position: Int)
    }

    interface ComboListener {
        fun onItemClick(c : Combos, view: View, position: Int)
    }

    interface ControlListener {
        fun onItemClick(c : Control, view: View, position: Int)
    }

    interface PersonalListener {
        fun onItemClick(p : Personal, view: View, position: Int)
    }

    interface CicloListener {
        fun onItemClick(c : Ciclo, view: View, position: Int)
    }
}