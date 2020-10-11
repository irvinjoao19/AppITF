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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.*
import com.itfperu.appitf.data.viewModel.MedicoViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.adapters.*
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_direccion.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
private const val ARG_PARAM4 = "param4"

class DireccionFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabDireccion -> if (validacion == 1) {
                dialogDireccion(medicoId)
            } else
                itfViewModel.setError("Completar el primer formulario.")
            R.id.fabSave -> formValidate()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: MedicoViewModel
    private var medicoId: Int = 0
    private var usuarioId: Int = 0
    private var validacion: Int = 0
    private var solMedicoId: Int = 0
    private var tipoMedico: Int = 0
//    var viewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            medicoId = it.getInt(ARG_PARAM1)
            solMedicoId = it.getInt(ARG_PARAM2)
            usuarioId = it.getInt(ARG_PARAM3)
            tipoMedico = it.getInt(ARG_PARAM4)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_direccion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
//        viewPager = activity!!.findViewById(R.id.viewPager)
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(MedicoViewModel::class.java)

        itfViewModel.getMedicoById(medicoId).observe(viewLifecycleOwner, {
            if (it != null) {
                validacion = 1
            }
        })

        val direccionAdapter =
            DireccionAdapter(object : OnItemClickListener.MedicoDireccionListener {
                override fun onItemClick(m: MedicoDireccion, view: View, position: Int) {

                }
            })
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = direccionAdapter
        itfViewModel.getDireccionesById(medicoId).observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                fabSave.visibility = View.VISIBLE
            } else
                fabSave.visibility = View.GONE

            direccionAdapter.addItems(it)
        })

        fabDireccion.setOnClickListener(this)
        fabSave.setOnClickListener(this)

        itfViewModel.mensajeError.observe(viewLifecycleOwner, {
//            viewPager?.currentItem = 0
            Util.toastMensaje(context!!, it)
        })
        itfViewModel.mensajeSuccess.observe(viewLifecycleOwner, {
            Util.toastMensaje(context!!, it)
            if (it == "Medico Guardado") {
                activity!!.finish()
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int, param3: Int, param4: Int) =
            DireccionFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                    putInt(ARG_PARAM3, param3)
                    putInt(ARG_PARAM4, param4)
                }
            }
    }

    private fun dialogDireccion(id: Int) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_direccion, null)
        val layoutTitle: TextView = v.findViewById(R.id.layoutTitle)
        val editTextDepartamento: TextInputEditText = v.findViewById(R.id.editTextDepartamento)
        val editTextProvincia: TextInputEditText = v.findViewById(R.id.editTextProvincia)
        val editTextDistrito: TextInputEditText = v.findViewById(R.id.editTextDistrito)
        val editTextInstitucion: TextInputEditText = v.findViewById(R.id.editTextInstitucion)
        val editTextDireccion: TextInputEditText = v.findViewById(R.id.editTextDireccion)
        val editTextReferencia: TextInputEditText = v.findViewById(R.id.editTextReferencia)
        val fabDirection: ExtendedFloatingActionButton = v.findViewById(R.id.fabDirection)
        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        val m = MedicoDireccion()
        layoutTitle.text = String.format("Nueva Dirección")

        editTextDepartamento.setOnClickListener {
            spinnerDialog(1, "Departamento", m, editTextDepartamento)
        }
        editTextProvincia.setOnClickListener {
            spinnerDialog(2, "Provincia", m, editTextProvincia)
        }
        editTextDistrito.setOnClickListener {
            spinnerDialog(3, "Distrito", m, editTextDistrito)
        }

        fabDirection.setOnClickListener {
            m.medicoId = id
            m.nombreDepartamento = editTextDepartamento.text.toString()
            m.nombreProvincia = editTextProvincia.text.toString()
            m.nombreDistrito = editTextDistrito.text.toString()
            m.institucion = editTextInstitucion.text.toString()
            m.direccion = editTextDireccion.text.toString()
            m.referencia = editTextReferencia.text.toString()
            m.estado = 10
            m.active = 1
            if (itfViewModel.validateDireccion(m)) {
                dialog.dismiss()
            }
        }
    }

    private fun spinnerDialog(
        tipo: Int, title: String, m: MedicoDireccion, input: TextInputEditText
    ) {
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
                val departamentoAdapter =
                    DepartamentoAdapter(object : OnItemClickListener.DepartamentoListener {
                        override fun onItemClick(d: Departamento, view: View, position: Int) {
                            m.codigoDepartamento = d.codigoDepartamento
                            input.setText(d.nombre)
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
                    ProvinciaAdapter(object : OnItemClickListener.ProvinciaListener {
                        override fun onItemClick(p: Provincia, view: View, position: Int) {
                            m.codigoProvincia = p.codigoProvincia
                            input.setText(p.nombre)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = provinciaAdapter
                itfViewModel.getProvincias().observe(this, {
                    if (it.isNullOrEmpty()) {
                        itfViewModel.setError("Datos vacios favor de sincronizar")
                    }
                    provinciaAdapter.addItems(it)
                })
            }
            3 -> {
                val distritoAdapter =
                    DistritoAdapter(object : OnItemClickListener.DistritoListener {
                        override fun onItemClick(d: Distrito, view: View, position: Int) {
                            m.codigoDistrito = d.codigoDistrito
                            input.setText(d.nombre)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = distritoAdapter
                itfViewModel.getDistritos().observe(this, {
                    if (it.isNullOrEmpty()) {
                        itfViewModel.setError("Datos vacios favor de sincronizar")
                    }
                    distritoAdapter.addItems(it)
                })
            }
        }
    }

    private fun formValidate() {
        val s = SolMedico()
        s.solMedicoId = solMedicoId
        s.estadoSol = 11
        s.estado = 1
        s.usuarioId = usuarioId
        s.fecha = Util.getFecha()
        s.tipo = tipoMedico
        itfViewModel.validateSolMedico(s)
    }
}