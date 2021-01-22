package com.itfperu.appitf.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.itfperu.appitf.ui.fragments.edition.*

abstract class TabLayoutAdapter {

    class MedicoForm(
        fm: FragmentManager,
        var numberOfTabs: Int, var sid: Int, var id: Int,
        var usuarioId: Int, var t: Int, var e: Int,
        var tipoEnvio: Int
    ) :
        FragmentStatePagerAdapter(fm, numberOfTabs) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> GeneralFragment.newInstance(id, sid, usuarioId, t, e, tipoEnvio)
                1 -> DireccionFragment.newInstance(id, sid, usuarioId, t, e, tipoEnvio)
                else -> Fragment()
            }
        }

        override fun getCount(): Int {
            return numberOfTabs
        }
    }

    class VisitaForm(
        fm: FragmentManager, var numberOfTabs: Int, var id: Int, var usuarioId: Int, var estado: Int
    ) :
        FragmentStatePagerAdapter(fm, numberOfTabs) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> VisitaGeneralFragment.newInstance(id, usuarioId)
                1 -> VisitaProductoFragment.newInstance(id, usuarioId, estado)
                else -> Fragment()
            }
        }

        override fun getCount(): Int {
            return numberOfTabs
        }
    }

    class ReporteForm(fm: FragmentManager, var numberOfTabs: Int, var tipo: Int) :
        FragmentStatePagerAdapter(fm, numberOfTabs) {

        override fun getItem(position: Int): Fragment {
            return when (tipo) {
                0 -> when (position) {
                    0 -> ReporteFechaFragment.newInstance("", "")
                    1 -> ReporteMesFragment.newInstance("", "")
                    else -> Fragment()
                }
                else -> when (position) {
                    0 -> ReporteSubFechaFragment.newInstance("", "")
                    1 -> ReporteSubMesFragment.newInstance("", "")
                    else -> Fragment()
                }
            }

        }

        override fun getCount(): Int {
            return numberOfTabs
        }
    }
}