package com.itfperu.appitf.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.itfperu.appitf.R
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
            R.id.pass -> Util.toastMensaje(this, "En desarrollo")
        }
        return false
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var usuarioViewModel: UsuarioViewModel
    private var usuarioId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
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
            startActivity(
                Intent(this, PreviewCameraActivity::class.java)
                    .putExtra("nameImg", it)
                    .putExtra("usuarioId", usuarioId)
                    .putExtra("galery", true)
            )
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
}