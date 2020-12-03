package com.itfperu.appitf.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Accesos
import com.itfperu.appitf.data.local.model.Usuario
import com.itfperu.appitf.data.viewModel.UsuarioViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.fragments.*
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.toolbar
import kotlinx.android.synthetic.main.nav_header_main.view.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var usuarioViewModel: UsuarioViewModel
    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null
    private var usuarioId: Int = 0
    private var tipo: Int  = 0
    private var logout: String = "off"

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.filtro, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindUI()
    }

    private fun bindUI() {
        usuarioViewModel =
            ViewModelProvider(this, viewModelFactory).get(UsuarioViewModel::class.java)
        usuarioViewModel.user.observe(this, { u ->
            if (u != null) {
                getUser(u)
                setSupportActionBar(toolbar)
                val toggle = ActionBarDrawerToggle(
                    this@MainActivity,
                    drawerLayout,
                    toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close
                )
                drawerLayout.addDrawerListener(toggle)
                toggle.syncState()
                navigationView.setNavigationItemSelectedListener(this@MainActivity)
                navigationView.menu.clear()

                val menu = navigationView.menu
                var subMenu: SubMenu?
                subMenu = menu.addSubMenu("Menu Principal")
                subMenu.add("Inicio")
                subMenu.getItem(0).setIcon(R.drawable.ic_points)
                subMenu.add("Descargar Información")
                subMenu.getItem(1).setIcon(R.drawable.ic_points)
                var position = 0
                usuarioViewModel.getAccesos(u.usuarioId).observe(this, { accesos ->
                    for (a: Accesos in accesos) {
                        if (a.tipo == 1) {
                            subMenu = menu.addSubMenu(a.nombre)
                            position = 0
                        } else {
                            subMenu!!.add(a.nombre)
                            subMenu!!.getItem(position).setIcon(R.drawable.ic_points)
                            position++
                        }
                    }
                    subMenu = menu.addSubMenu("")
                    subMenu!!.add("Cerrar Sesión")
                    subMenu!!.getItem(0).setIcon(R.drawable.ic_exit)
                    navigationView.invalidate()
                })

                if (u.esSupervisorId == 0) {
                    fragmentByDefault(ReporteFragment.newInstance(usuarioId, 0), "Reporte RR-MM")
                } else {
                    fragmentByDefault(ReporteFragment.newInstance(usuarioId, 1), "Reporte Supervisor")
                }

                message()
            } else {
                goLogin()
            }
        })
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.title) {
            "Roles" -> changeFragment(
                PerfilFragment.newInstance(usuarioId), item.title.toString()
            )
            "Usuarios" -> changeFragment(
                PersonalFragment.newInstance(usuarioId), item.title.toString()
            )
            "Feriados" -> changeFragment(
                FeriadoFragment.newInstance(usuarioId), item.title.toString()
            )
            "Monedas" -> changeFragment(
                MonedaFragment.newInstance(usuarioId), item.title.toString()
            )
            "Categorías" -> changeFragment(
                CategoriaFragment.newInstance(usuarioId), item.title.toString()
            )
            "Especialidades" -> changeFragment(
                EspecialidadFragment.newInstance(usuarioId), item.title.toString()
            )
            "Tipos de Productos" -> changeFragment(
                TipoProductoFragment.newInstance(usuarioId), item.title.toString()
            )
            "Resultados de Visita" -> changeFragment(
                VisitaFragment.newInstance(usuarioId), item.title.toString()
            )
            "Productos" -> changeFragment(
                ProductoFragment.newInstance(usuarioId), item.title.toString()
            )
            "Ciclos" -> changeFragment(
                CicloFragment.newInstance(usuarioId), item.title.toString()
            )
            "Actividades" -> changeFragment(
                ActividadFragment.newInstance(usuarioId, 1), item.title.toString()
            )
            "Aprobación actividades" -> changeFragment(
                ActividadFragment.newInstance(usuarioId, 2), item.title.toString()
            )
            "Nuevos médicos" -> changeFragment(
                MedicosFragment.newInstance(usuarioId, 1), item.title.toString()
            )
            "Aprobación médicos" -> changeFragment(
                MedicosFragment.newInstance(usuarioId, 2), item.title.toString()
            )
            "Target" -> changeFragment(
                TargetFragment.newInstance(usuarioId, 2), item.title.toString()
            )
            "Altas" -> changeFragment(
                TargetAltasFragment.newInstance(usuarioId, "A", 1), item.title.toString()
            )
            "Bajas" -> changeFragment(
                TargetAltasFragment.newInstance(usuarioId, "B", 1), item.title.toString()
            )
            "Aprobación altas" -> changeFragment(
                TargetAltasFragment.newInstance(usuarioId, "A", 2), item.title.toString()
            )
            "Aprobación bajas" -> changeFragment(
                TargetAltasFragment.newInstance(usuarioId, "B", 2), item.title.toString()
            )
            "Programación y reporte" -> changeFragment(
                ProgramacionFragment.newInstance(usuarioId), item.title.toString()
            )
            "Nuevas Direcciones" -> changeFragment(
                DireccionesFragment.newInstance(usuarioId, 1), item.title.toString()
            )
            "Aprobación Direcciones" -> changeFragment(
                DireccionesFragment.newInstance(usuarioId, 2), item.title.toString()
            )
            "Punto Contacto" -> changeFragment(
                PuntoContactoFragment.newInstance(usuarioId), item.title.toString()
            )
            "Stock" -> changeFragment(
                StockFragment.newInstance(usuarioId), item.title.toString()
            )
            "Reporte diario"->changeFragment(
                ReporteDiarioFragment.newInstance(usuarioId,tipo), item.title.toString()
            )
            "Descargar Información" -> dialogFunction(1, "Deseas Sincronizar ?")
            "Cerrar Sesión" -> dialogFunction(3, "Deseas Salir ?")
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun load(title: String) {
        builder = AlertDialog.Builder(ContextThemeWrapper(this@MainActivity, R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(this@MainActivity).inflate(R.layout.dialog_login, null)
        builder.setView(view)
        val textViewTitle: TextView = view.findViewById(R.id.textView)
        textViewTitle.text = title
        dialog = builder.create()
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    private fun closeLoad() {
        if (dialog != null) {
            if (dialog!!.isShowing) {
                dialog!!.dismiss()
            }
        }
    }

    private fun changeFragment(fragment: Fragment, title: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit()
        supportActionBar!!.title = title
    }

    private fun fragmentByDefault(f: Fragment, title: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content_frame, f)
            .commit()
        supportActionBar!!.title = title
    }

    private fun getUser(u: Usuario) {
        val header = navigationView.getHeaderView(0)
        header.textViewName.text = u.nombre
        header.textViewEmail.text = u.email
        usuarioId = u.usuarioId
        tipo = u.esSupervisorId

        header.textViewName.setOnClickListener { goPerfil() }
        header.textViewEmail.setOnClickListener { goPerfil() }

        Picasso.get().load(String.format("%s%s", Util.UrlFoto, u.foto))
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .into(header.imageView)
    }

    private fun goPerfil() {
        startActivity(
            Intent(this, PerfilActivity::class.java)
                .putExtra("usuarioId", usuarioId)
        )
    }

    private fun goLogin() {
        if (logout == "off") {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun message() {
        usuarioViewModel.mensajeSuccess.observe(this, { s ->
            if (s != null) {
                closeLoad()
                if (s == "Close") {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Util.toastMensaje(this, s)
                }
            }
        })
        usuarioViewModel.mensajeError.observe(this@MainActivity, { s ->
            if (s != null) {
                closeLoad()
                Util.snackBarMensaje(window.decorView, s)
            }
        })
    }

    private fun dialogFunction(tipo: Int, title: String) {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Mensaje")
            .setMessage(title)
            .setPositiveButton("SI") { dialog, _ ->
                when (tipo) {
                    1 -> {
                        load("Sincronizando..")
                        usuarioViewModel.sync(usuarioId)
                    }
//                    2 -> {
//                        load("Enviando..")
//                        usuarioViewModel.sendData(this)
//                    }
                    3 -> {
                        logout = "on"
                        load("Cerrando Session")
                        usuarioViewModel.logout(usuarioId.toString())
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("NO") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
    }
}