package com.itfperu.appitf.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.itfperu.appitf.R
import com.itfperu.appitf.data.viewModel.ProgramacionViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.adapters.TabLayoutAdapter
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_medico.*
import javax.inject.Inject

class VisitaActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: ProgramacionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visita)
        val b = intent.extras
        if (b != null){
            bindUI(b.getInt("programacionId"),b.getInt("u"))
        }
    }

    private fun bindUI(id:Int,u:Int){
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Nueva Visita"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab1))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab3))

        val tabLayoutAdapter =
            TabLayoutAdapter.VisitaForm(
                supportFragmentManager, tabLayout.tabCount, id, u
            )
        viewPager.adapter = tabLayoutAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
                Util.hideKeyboard(this@VisitaActivity)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}