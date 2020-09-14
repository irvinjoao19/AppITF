package com.itfperu.appitf.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.itfperu.appitf.R
import com.itfperu.appitf.ui.fragments.*
import kotlinx.android.synthetic.main.activity_form.*

class FormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        val b = intent.extras
        if (b != null) {
            bindUI(b.getString("title")!!)
            when (b.getInt("tipo")) {
                1 -> replaceFragment(
                    savedInstanceState, EditPerfilFragment.newInstance("", "")
                )
//                2 -> replaceFragment(
//                    savedInstanceState, PersonalMapFragment.newInstance(
//                        b.getInt("servicioId"), b.getInt("tipoId"), b.getInt("proveedorId")
//                    )
//                )
            }

        }
    }

    private fun bindUI(title: String) {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun replaceFragment(savedInstanceState: Bundle?, f: Fragment) {
        savedInstanceState ?: supportFragmentManager.beginTransaction()
            .replace(R.id.container, f).commit()
    }
}