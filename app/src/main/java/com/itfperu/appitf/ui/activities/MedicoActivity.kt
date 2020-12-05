package com.itfperu.appitf.ui.activities

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.itfperu.appitf.R
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.adapters.TabLayoutAdapter
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_medico.*

class MedicoActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medico)
        val b = intent.extras
        if (b != null) {
            bindUI(
                b.getInt("usuarioId"),
                b.getInt("solMedicoId"),
                b.getInt("medicoId"),
                b.getString("title")!!,
                b.getInt("tipoMedico"),
                b.getInt("estado"),
                b.getInt("tipoEnvio")
            )
        }
    }

    private fun bindUI(u: Int, sid: Int, id: Int, title: String, t: Int, e: Int, tipoEnvio: Int) {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab1))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab2))

        val tabLayoutAdapter =
            TabLayoutAdapter.MedicoForm(
                supportFragmentManager, tabLayout.tabCount, sid, id, u, t, e, tipoEnvio
            )
        viewPager.adapter = tabLayoutAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
                Util.hideKeyboard(this@MedicoActivity)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}