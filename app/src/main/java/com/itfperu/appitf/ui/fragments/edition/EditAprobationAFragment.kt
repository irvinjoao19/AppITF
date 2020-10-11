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
import com.itfperu.appitf.data.local.model.Personal
import com.itfperu.appitf.data.viewModel.ActividadViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.adapters.CicloAdapter
import com.itfperu.appitf.ui.adapters.ComboPersonalAdapter
import com.itfperu.appitf.ui.adapters.DuracionAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_edit_aprobation_a.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditAprobationAFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.editTextCiclo -> spinnerDialog(1, "Ciclo")
            R.id.editTextFechaAc -> Util.getDateDialog(context!!, editTextFechaAc)
            R.id.editTextDuracion -> spinnerDialog(2, "Duración")
            R.id.fabAprobar -> formActividad(9, "Aprobada")
            R.id.fabRechazar -> formActividad(8, "Rechazada")
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
        return inflater.inflate(R.layout.fragment_edit_aprobation_a, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(ActividadViewModel::class.java)

        itfViewModel.getActividadById(categoriaId).observe(viewLifecycleOwner, {
            if (it != null) {
                p = it
                editTextCiclo.setText(it.nombreCiclo)
                editTextCiclo.isEnabled = false
                editTextUsuario.setText(it.usuario)
                editTextFechaAc.setText(it.fechaActividad)
                editTextDuracion.setText(it.descripcionDuracion)
                editTextDetalle.setText(it.detalle)
                editTextAprobador.setText(it.aprobador)

                if (it.aprobador.isEmpty()) {
                    itfViewModel.getNombreUsuario().observe(viewLifecycleOwner, { s ->
                        editTextAprobador.setText(s)
                    })
                }

                editTextObservacion.setText(it.observacion)
                if (it.estado == 7) {
                    fabAprobar.visibility = View.VISIBLE
                    fabRechazar.visibility = View.VISIBLE
                }
            }
        })

        fabAprobar.setOnClickListener(this)
        fabRechazar.setOnClickListener(this)
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

    private fun formActividad(e: Int, name: String) {
        p.estado = e
        p.descripcionEstado = name
        p.nombreCiclo = editTextCiclo.text.toString()
        p.usuario = editTextUsuario.text.toString()
        p.fechaActividad = editTextFechaAc.text.toString()
        p.fechaRespuesta = Util.getFecha()
        p.descripcionDuracion = editTextDuracion.text.toString()
        p.detalle = editTextDetalle.text.toString()
        p.aprobador = editTextAprobador.text.toString()
        p.observacion = editTextObservacion.text.toString()
        p.usuarioId = usuarioId
        p.tipoInterfaz = "M"
        p.active = 1
        p.tipo = 2
        itfViewModel.validateActividad(p)
    }

    private fun spinnerDialog(tipo: Int, title: String) {
        val builder =
            android.app.AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
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
                val cicloAdapter = CicloAdapter(object : OnItemClickListener.CicloListener {
                    override fun onItemClick(c: Ciclo, view: View, position: Int) {
                        p.cicloId = c.cicloId
                        editTextCiclo.setText(c.nombre)
                        dialog.dismiss()
                    }
                })
                recyclerView.adapter = cicloAdapter
                itfViewModel.getCicloProceso().observe(this, {
                    cicloAdapter.addItems(it)
                })
            }
            2 -> {
                val duracionAdapter =
                    DuracionAdapter(object : OnItemClickListener.DuracionListener {
                        override fun onItemClick(d: Duracion, view: View, position: Int) {
                            p.cicloId = d.duracionId
                            editTextCiclo.setText(d.descripcion)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = duracionAdapter
                itfViewModel.getDuracion().observe(this, {
                    duracionAdapter.addItems(it)
                })
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            EditAprobationAFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}