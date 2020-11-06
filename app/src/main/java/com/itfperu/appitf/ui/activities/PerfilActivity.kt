package com.itfperu.appitf.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Usuario
import com.itfperu.appitf.data.viewModel.UsuarioViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Permission
import com.itfperu.appitf.helper.Util
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_perfil.*
import javax.inject.Inject

class PerfilActivity : DaggerAppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.photo -> goCamera()
            R.id.galery -> goGalery()
            R.id.pass -> dialogPassword(u)
        }
        return false
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var usuarioViewModel: UsuarioViewModel
    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null
    private var usuarioId: Int = 0
    lateinit var u: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        u = Usuario()
        val b = intent.extras
        if (b != null) {
            bindUI(b.getInt("usuarioId"))
        }
    }

    private fun bindUI(id: Int) {
        usuarioId = id
        usuarioViewModel =
            ViewModelProvider(this, viewModelFactory).get(UsuarioViewModel::class.java)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Perfil de Usuario"
        toolbar.setNavigationOnClickListener { finish() }

        usuarioViewModel.getUsuarioById(id).observe(this, {
            u = it
            Picasso.get().load(String.format("%s%s", Util.UrlFoto, it.foto))
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(imageViewPortada)

            textViewCodigo.text = it.login
            textViewNombre.text = String.format("%s %s %s", it.nombre, it.apellidoP, it.apellidoM)
            textViewDni.text = it.nroDoc
            textViewFechaN.text = it.fechaNacimiento
            textViewSexo.text = if (it.sexo == "M") "Masculino" else "Femenino"
            textViewRol.text = it.nombrePerfil
            textViewCelular.text = it.celular
            textViewEmail.text = it.email

            if (it.esSupervisorId == 0) {
                textViewSupervisor.text = it.nombreSupervisor
            }
        })

        usuarioViewModel.mensajeSuccess.observe(this, {
            closeLoad()
            if (it != "Actualizado") {
                startActivity(
                    Intent(this, PreviewCameraActivity::class.java)
                        .putExtra("nameImg", it)
                        .putExtra("usuarioId", usuarioId)
                        .putExtra("galery", true)
                )
            } else {
                Util.toastMensaje(this, it)
            }
        })
        usuarioViewModel.mensajeError.observe(this, {
            closeLoad()
            Util.toastMensaje(this, it)
        })
        bottomNavigation.setOnNavigationItemSelectedListener(this)
        fabEdit.setOnClickListener(this)
    }

    private fun goCamera() {
        startActivity(
            Intent(this, CameraActivity::class.java)
                .putExtra("usuarioId", usuarioId)
        )
    }

    private fun goGalery() {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(i, Permission.GALERY_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Permission.GALERY_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                usuarioViewModel.generarArchivo(
                    Util.getFechaActualForPhoto(usuarioId),
                    this,
                    data
                )
            }
        }
    }

    override fun onClick(v: View) {
        startActivity(
            Intent(this, FormActivity::class.java)
                .putExtra("title", "Modificar Usuario")
                .putExtra("tipo", 10)
                .putExtra("id", usuarioId)
                .putExtra("uId", usuarioId)
        )
    }

    private fun dialogPassword(u: Usuario) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(this).inflate(R.layout.dialog_password, null)
        val editTextPassActual: TextInputEditText = view.findViewById(R.id.editTextPassActual)
        val editTextPassNuevo: TextInputEditText = view.findViewById(R.id.editTextPassNuevo)
        val editTextConfirmacionPass: TextInputEditText =
            view.findViewById(R.id.editTextConfirmacionPass)
        val fab: ExtendedFloatingActionButton = view.findViewById(R.id.fab)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()

        fab.setOnClickListener {
            val passActual = editTextPassActual.text.toString()
            if (u.pass != passActual) {
                usuarioViewModel.setError("Contraseña no coincide con la contraseña actual")
                return@setOnClickListener
            }
            val newPass = editTextPassNuevo.text.toString()
            if (newPass.isEmpty()) {
                usuarioViewModel.setError("Ingrese nueva contraseña")
                return@setOnClickListener
            }
            val confirmPass = editTextConfirmacionPass.text.toString()
            if (confirmPass != newPass) {
                usuarioViewModel.setError("Confirmación de contraseña no coincide con nueva contraseña")
                return@setOnClickListener
            }
            u.pass = newPass
            load()
            usuarioViewModel.updateUsuario(u, this)
            dialog.dismiss()
        }
    }

    private fun load() {
        builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(this).inflate(R.layout.dialog_login, null)
        builder.setView(view)
        val textViewTitle: TextView = view.findViewById(R.id.textView)
        textViewTitle.text = String.format("Actualizando...")
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
}