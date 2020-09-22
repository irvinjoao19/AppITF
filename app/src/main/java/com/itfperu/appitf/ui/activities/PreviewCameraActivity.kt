package com.itfperu.appitf.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Usuario
import com.itfperu.appitf.data.local.model.Visita
import com.itfperu.appitf.data.viewModel.UsuarioViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_perfil.*
import kotlinx.android.synthetic.main.activity_preview_camera.*
import java.io.File
import javax.inject.Inject

class PreviewCameraActivity : DaggerAppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabOk -> confirmEnvio()
            R.id.fabClose -> if (galery) {
                finish()
            } else {
                startActivity(
                    Intent(this, CameraActivity::class.java)
                        .putExtra("usuarioId", usuarioId)
                )
                finish()
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var usuarioViewModel: UsuarioViewModel
    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null
    private var nameImg: String = ""
    private var usuarioId: Int = 0
    private var galery: Boolean = false
    lateinit var u: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_camera)
        val b = intent.extras
        if (b != null) {
            u = Usuario()
            nameImg = b.getString("nameImg")!!
            galery = b.getBoolean("galery")
            bindUI(b.getInt("usuarioId"))
        }
    }

    private fun bindUI(id: Int) {
        usuarioId = id
        usuarioViewModel =
            ViewModelProvider(this, viewModelFactory).get(UsuarioViewModel::class.java)

        fabClose.setOnClickListener(this)
        fabOk.setOnClickListener(this)

        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                val f = File(Util.getFolder(this), nameImg)
                Picasso.get().load(f)
                    .into(imageView, object : Callback {
                        override fun onSuccess() {
                            progressBar.visibility = View.GONE
                        }

                        override fun onError(e: Exception?) {
                        }
                    })
            }, 200)
        }

        usuarioViewModel.getUsuarioById(id).observe(this, {
            u = it
        })

        usuarioViewModel.mensajeError.observe(this, {
            closeLoad()
            Util.toastMensaje(this, it)
        })

        usuarioViewModel.mensajeSuccess.observe(this, {
            closeLoad()
            finish()
        })
    }

    private fun confirmEnvio() {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Mensaje")
            .setMessage("Deseas actualizar foto de perfil ?")
            .setPositiveButton("SI") { dialog, _ ->
                u.foto = nameImg
                usuarioViewModel.updateUsuario(u,this)
                load()
                dialog.dismiss()
            }
            .setNegativeButton("NO") { dialog, _ ->
                dialog.cancel()
                finish()
            }
        dialog.show()
    }

    private fun load() {
        builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(this).inflate(R.layout.dialog_login, null)
        builder.setView(view)
        val textViewTitle: TextView = view.findViewById(R.id.textView)
        textViewTitle.text = String.format("Enviando...")
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