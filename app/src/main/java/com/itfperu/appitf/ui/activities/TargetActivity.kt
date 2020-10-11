package com.itfperu.appitf.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.*
import com.itfperu.appitf.data.viewModel.TargetViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.adapters.*
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_target.*
import javax.inject.Inject

class TargetActivity : DaggerAppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.editTextMedico -> spinnerDialog(1, "Medico")
            R.id.editTextCategoria -> spinnerDialog(2, "Categoria")
            R.id.editTextEspecialidad -> spinnerDialog(3, "Especialidad")
            R.id.fabGenerate -> formTarget()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: TargetViewModel
    lateinit var d: TargetDet
    private var cabId: Int = 0
    private var detId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target)
        d = TargetDet()
        val b = intent.extras
        if (b != null) {
            cabId = b.getInt("targetCab")
            detId = b.getInt("targetDet")
            bindUI(b.getString("tipoTarget")!!)
        }
    }

    private fun bindUI(tipoTarget: String) {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(TargetViewModel::class.java)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Nuevo Médico"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        editTextMedico.setOnClickListener(this)
        editTextCategoria.setOnClickListener(this)
        editTextEspecialidad.setOnClickListener(this)
        fabGenerate.setOnClickListener(this)

        if (tipoTarget == "B") {
            layout4.visibility = View.GONE
            editTextContacto.visibility = View.GONE
        }

        itfViewModel.mensajeError.observe(this, {
            Util.toastMensaje(this, it)
        })
        itfViewModel.mensajeSuccess.observe(this, {
            finish()
            Util.toastMensaje(this, it)
        })
    }


    private fun spinnerDialog(tipo: Int, title: String) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(this).inflate(R.layout.dialog_combo, null)
        val progressBar: ProgressBar = v.findViewById(R.id.progressBar)
        val textViewTitulo: TextView = v.findViewById(R.id.textViewTitulo)
        val recyclerView: RecyclerView = v.findViewById(R.id.recyclerView)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        progressBar.visibility = View.GONE
        builder.setView(v)
        val dialog = builder.create()
        dialog.show()
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context, DividerItemDecoration.VERTICAL
            )
        )
        textViewTitulo.text = title

        when (tipo) {
            1 -> {
                val medicoAdapter = ComboMedicoAdapter(object : OnItemClickListener.MedicoListener {
                    override fun onItemClick(m: Medico, view: View, position: Int) {
                        d.medicoId = m.medicoId
                        d.cmp = m.cpmMedico
                        editTextMedico.setText(m.nombreMedico)
                        dialog.dismiss()
                    }
                })
                recyclerView.adapter = medicoAdapter
                itfViewModel.getMedicos().observe(this, {
                    if (it.isNullOrEmpty()) {
                        itfViewModel.setError("Datos vacios favor de sincronizar")
                    }
                    medicoAdapter.addItems(it)
                })
            }
            2 -> {
                val categoriaAdapter =
                    ComboCategoriaAdapter(object : OnItemClickListener.CategoriaListener {
                        override fun onItemClick(c: Categoria, view: View, position: Int) {
                            d.categoriaId = c.categoriaId
                            editTextCategoria.setText(c.descripcion)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = categoriaAdapter
                itfViewModel.getCategorias().observe(this, {
                    if (it.isNullOrEmpty()) {
                        itfViewModel.setError("Datos vacios favor de sincronizar")
                    }
                    categoriaAdapter.addItems(it)
                })
            }
            3 -> {
                val especialidadAdapter =
                    ComboEspecialidadAdapter(object : OnItemClickListener.EspecialidadListener {
                        override fun onItemClick(e: Especialidad, view: View, position: Int) {
                            d.especialidadId = e.especialidadId
                            editTextEspecialidad.setText(e.descripcion)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = especialidadAdapter
                itfViewModel.getEspecialidades().observe(this, {
                    if (it.isNullOrEmpty()) {
                        itfViewModel.setError("Datos vacios favor de sincronizar")
                    }
                    especialidadAdapter.addItems(it)
                })
            }
        }
    }

    private fun formTarget() {
        d.targetCabId = cabId
        d.targetDetId = detId
        d.nombreCategoria = editTextCategoria.text.toString()
        d.nombreEspecialidad = editTextEspecialidad.text.toString()
        d.nombreMedico = editTextMedico.text.toString()
        when {
            editTextContacto.text.toString().isEmpty() -> d.nroContacto = 0
            else -> d.nroContacto = editTextContacto.text.toString().toInt()
        }
        itfViewModel.validateTargetDet(d)
    }
}