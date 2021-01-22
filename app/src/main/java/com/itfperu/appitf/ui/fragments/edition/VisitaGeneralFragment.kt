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
import androidx.viewpager.widget.ViewPager
import com.google.android.material.radiobutton.MaterialRadioButton
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.*
import com.itfperu.appitf.data.viewModel.ProgramacionViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Gps
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.adapters.ComboDireccionAdapter
import com.itfperu.appitf.ui.adapters.ComboVisitaAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_visita_general.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class VisitaGeneralFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.editTextFechaProgramacion -> Util.getDateDialog(
                requireContext(), editTextFechaProgramacion
            )
            R.id.editTextHoraProgramacion -> Util.getHourDialog(requireContext(), editTextHoraProgramacion)
            R.id.editTextFechaReporte -> Util.getDateDialog(requireContext(), editTextFechaReporte)
            R.id.editTextHoraReporte -> Util.getHourDialog(requireContext(), editTextHoraReporte)

            R.id.editTextDireccion -> spinnerDialog(2, "Direcciones del Medico")
            R.id.editTextResultado -> spinnerDialog(1, "Resultado de Visitas")
            R.id.radioSI -> {
                layout14.visibility = View.VISIBLE
                layoutAcompanate.visibility = View.VISIBLE
                editTextAcompanate.visibility = View.VISIBLE
                clearCheck(R.id.radioSI)
            }
            R.id.radioNO -> {
                layout14.visibility = View.VISIBLE
                layoutAcompanate.visibility = View.GONE
                editTextAcompanate.visibility = View.GONE
                clearCheck(R.id.radioNO)
            }
            R.id.fabGenerate -> formProgramacion()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: ProgramacionViewModel
    lateinit var p: Programacion
    private var usuarioId: Int = 0
    private var programacionId: Int = 0
    private var medicoId: Int = 0
    private var viewPager: ViewPager? = null
    private var childrenInOrder = ArrayList<MaterialRadioButton>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        p = Programacion()
        arguments?.let {
            programacionId = it.getInt(ARG_PARAM1)
            usuarioId = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_visita_general, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        viewPager = requireActivity().findViewById(R.id.viewPager)
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(ProgramacionViewModel::class.java)

        itfViewModel.getProgramacionById(programacionId).observe(viewLifecycleOwner, {
            if (it != null) {
                p = it
                medicoId = it.medicoId
                editTextCiclo.setText(it.nombreCiclo)
                editTextVisita.setText(it.numeroVisita)
                editTextVisitador.setText(it.nombreUsuario)
                editTextCMP.setText(it.cmpMedico)
                editTextMedico.setText(it.nombreMedico)
                editTextCategoria.setText(it.categoria)
                editTextEspecialidad.setText(it.especialidad)
                editTextDireccion.setText(it.direccion)

                if (it.fechaProgramacion != "01/01/1900") {
                    editTextFechaProgramacion.setText(it.fechaProgramacion)
                    editTextHoraProgramacion.setText(it.horaProgramacion)
                }

                if (it.fechaReporteProgramacion != "01/01/1900") {
                    editTextFechaReporte.setText(it.fechaReporteProgramacion)
                    editTextHoraReporte.setText(it.horaReporteProgramacion)
                }

                if (it.visitaAcompanada == "SI") {
                    radioSI.isChecked = true
                    radioNO.isChecked = false
                    layout14.visibility = View.VISIBLE
                    layoutAcompanate.visibility = View.VISIBLE
                    editTextAcompanate.visibility = View.VISIBLE
                    editTextAcompanate.setText(it.dataAcompaniante)
                } else {
                    layout14.visibility = View.GONE
                    layoutAcompanate.visibility = View.GONE
                    editTextAcompanate.visibility = View.GONE
                    radioSI.isChecked = false
                    radioNO.isChecked = true
                }
                if (it.descripcionResultado.isEmpty()) {
                    editTextResultado.setText(String.format("Visitado"))
                    p.resultadoVisitaId = 13
                } else {
                    editTextResultado.setText(it.descripcionResultado)
                }

                if (it.estadoProgramacion == 24) {
                    fabGenerate.visibility = View.GONE
                }
            }
        })

        itfViewModel.mensajeSuccess.observe(viewLifecycleOwner, {
            viewPager?.currentItem = 1
            Util.toastMensaje(requireContext(), it)
        })
        itfViewModel.mensajeError.observe(viewLifecycleOwner, {
            Util.toastMensaje(requireContext(), it)
        })

        editTextFechaProgramacion.setOnClickListener(this)
        editTextFechaReporte.setOnClickListener(this)
        editTextHoraProgramacion.setOnClickListener(this)
        editTextHoraReporte.setOnClickListener(this)

        editTextDireccion.setOnClickListener(this)
        radioSI.setOnClickListener(this)
        radioNO.setOnClickListener(this)
        editTextResultado.setOnClickListener(this)
        fabGenerate.setOnClickListener(this)

        childrenInOrder.add(radioSI)
        childrenInOrder.add(radioNO)
    }

    private fun formProgramacion() {
        val gps = Gps(requireContext())
        if (gps.isLocationEnabled()) {
            if (gps.latitude.toString() != "0.0" || gps.longitude.toString() != "0.0") {
                p.nombreCiclo = editTextCiclo.text.toString()
                p.numeroVisita = editTextVisita.text.toString()
                p.nombreUsuario = editTextVisitador.text.toString()
                p.cmpMedico = editTextCMP.text.toString()
                p.nombreMedico = editTextMedico.text.toString()
                p.categoria = editTextCategoria.text.toString()
                p.especialidad = editTextEspecialidad.text.toString()
                p.direccion = editTextDireccion.text.toString()
                p.fechaProgramacion = editTextFechaProgramacion.text.toString()
                p.horaProgramacion = editTextHoraProgramacion.text.toString()
                p.fechaReporteProgramacion = editTextFechaReporte.text.toString()
                p.horaReporteProgramacion = editTextHoraReporte.text.toString()
                p.visitaAcompanada = when {
                    radioSI.isChecked -> "SI"
                    radioNO.isChecked -> "NO"
                    else -> ""
                }
                p.dataAcompaniante = editTextAcompanate.text.toString()
                p.descripcionResultado = editTextResultado.text.toString()
                p.latitud = gps.latitude.toString()
                p.longitud = gps.longitude.toString()
                p.active = 2
                itfViewModel.validateProgramacion(p)
            }
        } else {
            gps.showSettingsAlert(requireContext())
        }
    }

    private fun clearCheck(id: Int) {
        for (i in 0 until childrenInOrder.size) {
            val child = childrenInOrder[i]
            if (child.id != id) {
                child.isChecked = false
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            VisitaGeneralFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }

    private fun spinnerDialog(tipo: Int, title: String) {
        val builder =
            AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
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
                val visitaAdapter = ComboVisitaAdapter(object : OnItemClickListener.VisitaListener {
                    override fun onItemClick(v: Visita, view: View, position: Int) {
                        p.resultadoVisitaId = v.visitaId
                        editTextResultado.setText(v.descripcion)
                        dialog.dismiss()
                    }
                })
                recyclerView.adapter = visitaAdapter
                itfViewModel.getVisitas().observe(viewLifecycleOwner, {
                    if (it.isNullOrEmpty()) {
                        itfViewModel.setError("Datos vacios favor de sincronizar")
                    }
                    visitaAdapter.addItems(it)
                })
            }
            2 -> {
                val direccionAdapter =
                    ComboDireccionAdapter(object : OnItemClickListener.MedicoDireccionListener {
                        override fun onItemClick(m: MedicoDireccion, view: View, position: Int) {
                            p.direccionId = m.medicoDireccionId
                            editTextDireccion.setText(
                                String.format(
                                    "%s , %s - %s - %s", m.direccion, m.nombreDistrito,
                                    m.nombreProvincia, m.nombreDepartamento
                                )
                            )
                            dialog.dismiss()
                        }
                    })

                recyclerView.adapter = direccionAdapter
                itfViewModel.getDireccionById(medicoId).observe(viewLifecycleOwner, {
                    if (it.isNullOrEmpty()) {
                        itfViewModel.setError("Datos vacios favor de sincronizar")
                    }
                    direccionAdapter.addItems(it)
                })
            }
        }
    }
}