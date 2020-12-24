package com.itfperu.appitf.ui.fragments.edition

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Actividad
import com.itfperu.appitf.data.local.model.Ciclo
import com.itfperu.appitf.data.local.model.Duracion
import com.itfperu.appitf.data.local.model.Medico
import com.itfperu.appitf.data.viewModel.ActividadViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.adapters.ComboCicloAdapter
import com.itfperu.appitf.ui.adapters.ComboMedicoAdapter
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
            R.id.editTextFechaAc -> Util.getDateDialog(requireContext(), editTextFechaAc)
            R.id.editTextDuracion -> spinnerDialog(2, "Duración")
            R.id.editTextMedico -> spinnerDialog(3, "Médico")
            R.id.fabGenerate -> formActividad()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: ActividadViewModel
    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null
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
                editTextMedico.setText(it.nombreMedico)

                if (it.estado == 8 || it.estado == 9) {
                    layout6.visibility = View.VISIBLE
                    layout7.visibility = View.VISIBLE
                    layoutAprobador.visibility = View.VISIBLE
                    layoutObservacion.visibility = View.VISIBLE
                }
            } else {
                itfViewModel.getFirstCicloProceso().observe(viewLifecycleOwner, { c ->
                    if (c != null) {
                        p.cicloId = c.cicloId
                        editTextCiclo.setText(c.nombre)
                    }
                })
            }
        })

        fabGenerate.setOnClickListener(this)
        editTextCiclo.setOnClickListener(this)
        editTextFechaAc.setOnClickListener(this)
        editTextDuracion.setOnClickListener(this)
        editTextMedico.setOnClickListener(this)

        itfViewModel.mensajeError.observe(viewLifecycleOwner, {
            closeLoad()
            Util.toastMensaje(requireContext(), it)
        })

        itfViewModel.mensajeSuccess.observe(viewLifecycleOwner, {
            closeLoad()
            Util.toastMensaje(requireContext(), it)
            requireActivity().finish()
        })
    }

    private fun formActividad() {
        p.nombreCiclo = editTextCiclo.text.toString()
        p.fechaActividad = editTextFechaAc.text.toString()
        p.descripcionDuracion = editTextDuracion.text.toString()
        p.detalle = editTextDetalle.text.toString()
        p.descripcionEstado = editTextEstado.text.toString()
        p.nombreMedico = editTextMedico.text.toString()
        p.usuarioId = usuarioId
        p.tipoInterfaz = "M"
        p.active = 0
        p.tipo = 1
        load()
        itfViewModel.validateActividad(p)
    }

    private fun spinnerDialog(tipo: Int, title: String) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_combo, null)
        val progressBar: ProgressBar = v.findViewById(R.id.progressBar)
        val textViewTitulo: TextView = v.findViewById(R.id.textViewTitulo)
        val recyclerView: RecyclerView = v.findViewById(R.id.recyclerView)
        val layoutSearch: TextInputLayout = v.findViewById(R.id.layoutSearch)
        val editTextSearch: TextInputEditText = v.findViewById(R.id.editTextSearch)
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
            3 -> {
                layoutSearch.visibility = View.VISIBLE
                val medicoAdapter = ComboMedicoAdapter(object : OnItemClickListener.MedicoListener {
                    override fun onItemClick(m: Medico, view: View, position: Int) {
                        itfViewModel.getAlertaActividad(p.cicloId, m.medicoId, usuarioId)
                        p.medicoId = m.medicoId
                        editTextMedico.setText(
                            String.format(
                                "%s %s %s", m.apellidoP, m.apellidoM, m.nombreMedico
                            )
                        )
                        dialog.dismiss()
                    }
                })
                recyclerView.adapter = medicoAdapter
                itfViewModel.getMedicosByEstado(1, usuarioId).observe(this, {
                    if (it.isNullOrEmpty()) {
                        itfViewModel.setError("Datos vacios favor de sincronizar Visita Medica")
                    }
                    medicoAdapter.addItems(it)
                })
                editTextSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun afterTextChanged(editable: Editable) {
                        medicoAdapter.getFilter().filter(editable)
                    }
                })
            }
        }
    }

    private fun load() {
        builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(context).inflate(R.layout.dialog_login, null)
        builder.setView(view)
        val textViewTitle: TextView = view.findViewById(R.id.textView)
        textViewTitle.text = String.format("Enviando..")
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