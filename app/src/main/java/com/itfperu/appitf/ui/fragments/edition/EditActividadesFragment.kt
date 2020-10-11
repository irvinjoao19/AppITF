package com.itfperu.appitf.ui.fragments.edition

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.itfperu.appitf.data.local.model.Actividad
import com.itfperu.appitf.data.local.model.Ciclo
import com.itfperu.appitf.data.local.model.Duracion
import com.itfperu.appitf.data.viewModel.ActividadViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.adapters.ComboCicloAdapter
import com.itfperu.appitf.ui.adapters.DuracionAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_edit_actividades.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditActividadesFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.editTextCiclo -> spinnerDialog(1, "Ciclo")
            R.id.editTextFechaAc -> Util.getDateDialog(context!!, editTextFechaAc)
            R.id.editTextDuracion -> spinnerDialog(2, "Duración")
            R.id.fabGenerate -> formActividad()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: ActividadViewModel
    lateinit var p: Actividad
    private var usuarioId: Int = 0
    private var categoriaId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        p = Actividad()
        arguments?.let {
            categoriaId = it.getInt(ARG_PARAM1)
            usuarioId = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_actividades, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(ActividadViewModel::class.java)

        p.estado = 7
        editTextEstado.setText(String.format("ENVIADA"))

        itfViewModel.getActividadById(categoriaId).observe(viewLifecycleOwner, {
            if (it != null) {
                p = it
                editTextCiclo.setText(it.nombreCiclo)
                editTextCiclo.isEnabled = false
                editTextFechaAc.setText(it.fechaActividad)
                editTextDuracion.setText(it.descripcionDuracion)
                editTextDetalle.setText(it.detalle)
                editTextEstado.setText(it.descripcionEstado)
                editTextEstado.isEnabled = false

                if (it.estado == 8 || it.estado == 9) {
                    layout5.visibility = View.VISIBLE
                    layout6.visibility = View.VISIBLE
                    layoutAprobador.visibility = View.VISIBLE
                    layoutObservacion.visibility = View.VISIBLE
                }

            }
        })

        fabGenerate.setOnClickListener(this)
        editTextCiclo.setOnClickListener(this)
        editTextFechaAc.setOnClickListener(this)
        editTextDuracion.setOnClickListener(this)

        itfViewModel.mensajeError.observe(viewLifecycleOwner, {
            Util.toastMensaje(context!!, it)
        })

        itfViewModel.mensajeSuccess.observe(viewLifecycleOwner, {
            Util.toastMensaje(context!!, it)
            activity!!.finish()
        })
    }

    private fun formActividad() {
        p.nombreCiclo = editTextCiclo.text.toString()
        p.fechaActividad = editTextFechaAc.text.toString()
        p.descripcionDuracion = editTextDuracion.text.toString()
        p.detalle = editTextDetalle.text.toString()
        p.descripcionEstado = editTextEstado.text.toString()
        p.usuarioId = usuarioId
        p.tipoInterfaz = "M"
        p.active = 1
        p.tipo = 1
        itfViewModel.validateActividad(p)
    }

    private fun spinnerDialog(tipo: Int, title: String) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_combo, null)
        val progressBar: ProgressBar = v.findViewById(R.id.progressBar)
        val textViewTitulo: TextView = v.findViewById(R.id.textViewTitulo)
        val recyclerView: RecyclerView = v.findViewById(R.id.recyclerView)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
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
                val cicloAdapter = ComboCicloAdapter(object : OnItemClickListener.CicloListener {
                    override fun onItemClick(c: Ciclo, view: View, position: Int) {
                        p.cicloId = c.cicloId
                        editTextCiclo.setText(c.nombre)
                        dialog.dismiss()
                    }
                })
                recyclerView.adapter = cicloAdapter
                itfViewModel.getCicloProceso().observe(this, {
                    if (it.isNullOrEmpty()) {
                        itfViewModel.setError("Datos vacios favor de sincronizar")
                    }
                    cicloAdapter.addItems(it)
                })
            }
            2 -> {
                val duracionAdapter =
                    DuracionAdapter(object : OnItemClickListener.DuracionListener {
                        override fun onItemClick(d: Duracion, view: View, position: Int) {
                            p.duracionId = d.duracionId
                            editTextDuracion.setText(d.descripcion)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = duracionAdapter
                itfViewModel.getDuracion().observe(this, {
                    if (it.isNullOrEmpty()) {
                        itfViewModel.setError("Datos vacios favor de sincronizar Visita Medica")
                    }
                    duracionAdapter.addItems(it)
                })
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            EditActividadesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}