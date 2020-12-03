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
        fun onItemClick(c: Combos, view: View, position: Int)
    }

    interface ControlListener {
        fun onItemClick(c: Control, view: View, position: Int)
    }

    interface PersonalListener {
        fun onItemClick(p: Personal, view: View, position: Int)
    }

    interface CicloListener {
        fun onItemClick(c: Ciclo, view: View, position: Int)
    }

    interface ActividadListener {
        fun onItemClick(a: Actividad, view: View, position: Int)
    }

    interface EstadoListener {
        fun onItemClick(e: Estado, view: View, position: Int)
    }

    interface DuracionListener {
        fun onItemClick(d: Duracion, view: View, position: Int)
    }

    interface SolMedicoListener {
        fun onItemClick(m: SolMedico, view: View, position: Int)
    }

    interface MedicoListener {
        fun onItemClick(m: Medico, view: View, position: Int)
    }

    interface CheckMedicoListener {
        fun onCheckedChanged(m: Medico, p: Int, b: Boolean)
    }

    interface MedicoDireccionListener {
        fun onItemClick(m: MedicoDireccion, view: View, position: Int)
    }

    interface IdentificadorListener {
        fun onItemClick(i: Identificador, view: View, position: Int)
    }

    interface TargetListener {
        fun onItemClick(t: TargetM, view: View, position: Int)
    }

    interface TargetAltasListener {
        fun onItemClick(t: TargetCab, view: View, position: Int)
    }

    interface TargetDetListener {
        fun onItemClick(t: TargetDet, view: View, position: Int)
    }

    interface UbigeoListener {
        fun onItemClick(u: Ubigeo, view: View, position: Int)
    }

    interface ProgramacionListener {
        fun onItemClick(p: Programacion, view: View, position: Int)
    }

    interface ProgramacionDetListener {
        fun onItemClick(p: ProgramacionDet, view: View, position: Int)
    }

    interface StockListener {
        fun onItemClick(s: Stock, view: View, position: Int)
    }

    interface NuevaDireccionListener {
        fun onItemClick(n: NuevaDireccion, view: View, position: Int)
    }

    interface ProgramacionPerfilListener {
        fun onItemClick(s: String)
    }

    interface PuntoContactoListener {
        fun onItemClick(p: PuntoContacto, view: View, position: Int)
    }
}