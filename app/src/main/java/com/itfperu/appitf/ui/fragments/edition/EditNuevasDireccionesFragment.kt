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
import com.itfperu.appitf.data.local.model.Medico
import com.itfperu.appitf.data.local.model.NuevaDireccion
import com.itfperu.appitf.data.local.model.Ubigeo
import com.itfperu.appitf.data.viewModel.DireccionViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.adapters.ComboMedicoAdapter
import com.itfperu.appitf.ui.adapters.MedicoAdapter
import com.itfperu.appitf.ui.adapters.UbigeoAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_edit_nuevas_direcciones.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditNuevasDireccionesFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.editTextMedico -> spinnerDialog(4, "Medico")
            R.id.editTextDepartamento -> spinnerDialog(1, "Departamento")
            R.id.editTextProvincia -> spinnerDialog(2, "Provincia")
            R.id.editTextDistrito -> spinnerDialog(3, "Distrito")
            R.id.fabSave -> formNuevaDireccion()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: DireccionViewModel
    lateinit var d: NuevaDireccion
    private var solMedicoId: Int = 0
    private var usuarioId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        d = NuevaDireccion()
        arguments?.let {
            solMedicoId = it.getInt(ARG_PARAM1)
            usuarioId = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_nuevas_direcciones, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(DireccionViewModel::class.java)


        itfViewModel.getNuevaDireccionId(solMedicoId).observe(viewLifecycleOwner, {
            if (it != null) {
                d = it
                editTextMedico.setText(it.nombreMedico)
                editTextDepartamento.setText(it.nombreDepartamento)
                editTextProvincia.setText(it.nombreProvincia)
                editTextDistrito.setText(it.nombreDistrito)
                editTextDireccion.setText(it.nombreDireccion)
                editTextInstitucion.setText(it.nombreInstitucion)
                editTextReferencia.setText(it.referencia)

                if (it.estadoId == 27 || it.estadoId == 28) {
                    fabSave.visibility = View.GONE
                }
            }
        })
        itfViewModel.mensajeSuccess.observe(viewLifecycleOwner, {
            Util.toastMensaje(context!!, it)
            activity!!.finish()
        })
        itfViewModel.mensajeError.observe(viewLifecycleOwner, {
            Util.toastMensaje(context!!, it)
        })
        editTextMedico.setOnClickListener(this)
        editTextDepartamento.setOnClickListener(this)
        editTextProvincia.setOnClickListener(this)
        editTextDistrito.setOnClickListener(this)
        editTextDireccion.setOnClickListener(this)
        fabSave.setOnClickListener(this)
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
                val departamentoAdapter =
                    UbigeoAdapter(1, object : OnItemClickListener.UbigeoListener {
                        override fun onItemClick(u: Ubigeo, view: View, position: Int) {
                            d.codigoDepartamento = u.codDepartamento
                            editTextDepartamento.setText(u.nombreDepartamento)
                            d.codigoProvincia = ""
                            d.codigoDistrito = ""
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = departamentoAdapter
                itfViewModel.getDepartamentos().observe(this, {
                    if (it.isNullOrEmpty()) {
                        itfViewModel.setError("Datos vacios favor de sincronizar")
                    }
                    departamentoAdapter.addItems(it)
                })
            }
            2 -> {
                val provinciaAdapter =
                    UbigeoAdapter(2, object : OnItemClickListener.UbigeoListener {
                        override fun onItemClick(u: Ubigeo, view: View, position: Int) {
                            d.codigoProvincia = u.codProvincia
                            editTextProvincia.setText(u.provincia)
                            d.codigoDistrito = ""
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = provinciaAdapter
                itfViewModel.getProvincias(d.codigoDepartamento).observe(this, {
                    if (it.isNullOrEmpty()) {
                        itfViewModel.setError("Datos vacios favor de sincronizar")
                    }
                    provinciaAdapter.addItems(it)
                })
            }
            3 -> {
                val distritoAdapter =
                    UbigeoAdapter(3, object : OnItemClickListener.UbigeoListener {
                        override fun onItemClick(u: Ubigeo, view: View, position: Int) {
                            d.codigoDistrito = u.codDistrito
                            editTextDistrito.setText(u.nombreDistrito)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = distritoAdapter
                itfViewModel.getDistritos(d.codigoDepartamento, d.codigoProvincia).observe(this, {
                    if (it.isNullOrEmpty()) {
                        itfViewModel.setError("Datos vacios favor de sincronizar")
                    }
                    distritoAdapter.addItems(it)
                })
            }
            4 -> {
                layoutSearch.visibility = View.VISIBLE
                val medicoAdapter = ComboMedicoAdapter(object : OnItemClickListener.MedicoListener {
                    override fun onItemClick(m: Medico, view: View, position: Int) {
                        d.medicoId = m.medicoId
                        editTextMedico.setText(
                            String.format(
                                "%s %s %s", m.nombreMedico, m.apellidoP, m.apellidoM
                            )
                        )
                        dialog.dismiss()
                    }
                })
                recyclerView.adapter = medicoAdapter
                itfViewModel.getMedicosByEstado(1).observe(this, {
                    if (it.isNullOrEmpty()) {
                        itfViewModel.setError("Datos vacios favor de sincronizar")
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

    private fun formNuevaDireccion() {
        d.usuarioId = usuarioId
        d.estadoId = 26
        d.descripcionEstado = "Enviada"
        d.tipo = 1
        d.active = 1
        d.fechaSolicitud = Util.getFecha()
        d.fechaRespuesta = Util.getFecha()
        d.nombreMedico = editTextMedico.text.toString()
        d.nombreDepartamento = editTextDepartamento.text.toString()
        d.nombreProvincia = editTextProvincia.text.toString()
        d.nombreDistrito = editTextDistrito.text.toString()
        d.nombreDireccion = editTextDireccion.text.toString()
        d.nombreInstitucion = editTextInstitucion.text.toString()
        d.referencia = editTextReferencia.text.toString()
        itfViewModel.validateNuevaDireccion(d)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            EditNuevasDireccionesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}