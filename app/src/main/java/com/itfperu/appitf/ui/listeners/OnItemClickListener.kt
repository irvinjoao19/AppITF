package com.itfperu.appitf.ui.listeners

import android.view.View
import com.itfperu.appitf.data.local.model.*

interface OnItemClickListener {

    interface PerfilListener {
        fun onItemClick(p : Perfil, view: View, position: Int)
    }

    interface MonedaListener {
        fun onItemClick(m : Moneda, view: View, position: Int)
    }

    interface FeriadoListener {
        fun onItemClick(f : Feriado, view: View, position: Int)
    }
}