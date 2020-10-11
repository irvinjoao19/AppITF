package com.itfperu.appitf.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.itfperu.appitf.ui.fragments.edition.GeneralFragment
import com.itfperu.appitf.ui.fragments.edition.DireccionFragment

abstract class TabLayoutAdapter {

    class TabLayoutForm(
        fm: FragmentManager,
        var numberOfTabs: Int, var sid: Int, var id: Int, var usuarioId: Int, var t: Int
    ) :
        FragmentStatePagerAdapter(fm, numberOfTabs) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> GeneralFragment.newInstance(id, sid, usuarioId)
                1 -> DireccionFragment.newInstance(id, sid, usuarioId, t)
                else -> Fragment()
            }
        }

        override fun getCount(): Int {
            return numberOfTabs
        }
    }

}