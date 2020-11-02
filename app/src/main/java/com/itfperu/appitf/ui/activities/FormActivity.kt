package com.itfperu.appitf.ui.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.itfperu.appitf.R
import com.itfperu.appitf.ui.fragments.edition.*
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_form.*

/**
 * 1 -> categoria
 * 2 -> especialidad
 * 3 -> feriado
 * 4 -> moneda
 * 5 -> perfil
 * 6 -> producto
 * 7 -> tipo producto
 * 8 -> visita
 * 9 -> personal
 * 10 -> usuario
 * 11 -> ciclos
 * 12 -> actividad
 * 13 -> aprobacion de actividades
 * 14 -> medico
 * 15 -> target alta o baja
 * 16 , 17 -> nuevas direccion y aprobacion de nuevas direcciones
 *
 */
class FormActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        val b = intent.extras
        if (b != null) {
            bindUI(b.getString("title")!!)
            when (b.getInt("tipo")) {
                1 -> replaceFragment(
                    savedInstanceState,
                    EditCategoriaFragment.newInstance(b.getInt("id"), b.getInt("uId"))
                )
                2 -> replaceFragment(
                    savedInstanceState,
                    EditEspecialidadFragment.newInstance(b.getInt("id"), b.getInt("uId"))
                )
                3 -> replaceFragment(
                    savedInstanceState,
                    EditFeriadoFragment.newInstance(b.getInt("id"), b.getInt("uId"))
                )
                4 -> replaceFragment(
                    savedInstanceState,
                    EditMonedaFragment.newInstance(b.getInt("id"), b.getInt("uId"))
                )
                5 -> replaceFragment(
                    savedInstanceState,
                    EditPerfilFragment.newInstance(b.getInt("id"), b.getInt("uId"))
                )
                6 -> replaceFragment(
                    savedInstanceState,
                    EditProductoFragment.newInstance(b.getInt("id"), b.getInt("uId"))
                )
                7 -> replaceFragment(
                    savedInstanceState,
                    EditTipoProductoFragment.newInstance(b.getInt("id"), b.getInt("uId"))
                )
                8 -> replaceFragment(
                    savedInstanceState,
                    EditVisitaFragment.newInstance(b.getInt("id"), b.getInt("uId"))
                )
                9 -> replaceFragment(
                    savedInstanceState,
                    EditPersonalFragment.newInstance(b.getInt("id"), b.getInt("uId"))
                )
                10 -> replaceFragment(
                    savedInstanceState,
                    EditUsuarioFragment.newInstance(b.getInt("id"), b.getInt("uId"))
                )
                11 -> replaceFragment(
                    savedInstanceState,
                    EditCicloFragment.newInstance(b.getInt("id"), b.getInt("uId"))
                )
                12 -> replaceFragment(
                    savedInstanceState,
                    EditActividadesFragment.newInstance(b.getInt("id"), b.getInt("uId"))
                )
                13 -> replaceFragment(
                    savedInstanceState,
                    EditAprobationAFragment.newInstance(b.getInt("id"), b.getInt("uId"))
                )
                14 -> replaceFragment(
                    savedInstanceState,
                    EditSolMedicoFragment.newInstance(b.getInt("id"), b.getInt("uId"),b.getInt("tipoMedico"),b.getInt("estado"))
                )
                15-> replaceFragment(
                    savedInstanceState,
                    EditTargetFragment.newInstance(b.getInt("id"), b.getInt("uId"),b.getString("tipoTarget")!!,b.getInt("tipoAprobacion"),b.getInt("estado"))
                )
                16-> replaceFragment(
                    savedInstanceState,
                    EditNuevasDireccionesFragment.newInstance(b.getInt("id"), b.getInt("uId"))
                )
                17-> replaceFragment(
                    savedInstanceState,
                    EditNuevasDireccionesAFragment.newInstance(b.getInt("id"), b.getInt("uId"))
                )
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