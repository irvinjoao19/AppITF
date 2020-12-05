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
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.*
import com.itfperu.appitf.data.viewModel.MedicoViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.adapters.*
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_general.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
private const val ARG_PARAM4 = "param4"
private const val ARG_PARAM5 = "param5"
private const val ARG_PARAM6 = "param6"

class GeneralFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.editTextIdentificador -> spinnerDialog(1, "Identificador")
            R.id.editTextCategoria -> spinnerDialog(2, "Categoria")
            R.id.editTextEspecialidad -> spinnerDialog(3, "Especialidad")
            R.id.editTextEspecialidad2 -> spinnerDialog(4, "Especialidad 2")
            R.id.editTextFechaN -> Util.getDateDialog(context!!, editTextFechaN)
            R.id.editTextSexo -> spinnerDialog(5, "Sexo")
            R.id.fabGenerate -> if (tipoEnvio == 0) formGenerateCabecera() else formValidateMedico()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: MedicoViewModel
    lateinit var m: Medico
    lateinit var s: SolMedico
    private var solMedicoId: Int = 0
    private var medicoId: Int = 0
    private var usuarioId: Int = 0
    private var estado: Int = 0
    private var tipoMedico: Int = 0
    private var tipoEnvio: Int = 0 //0-> sol medico,1 target solo actualizacion de medico
    private var viewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        s = SolMedico()
        m = Medico()
        arguments?.let {
            medicoId = it.getInt(ARG_PARAM1)
            solMedicoId = it.getInt(ARG_PARAM2)
            usuarioId = it.getInt(ARG_PARAM3)
            tipoMedico = it.getInt(ARG_PARAM4)
            estado = it.getInt(ARG_PARAM5)
            tipoEnvio = it.getInt(ARG_PARAM6)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_general, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        viewPager = activity!!.findViewById(R.id.viewPager)
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(MedicoViewModel::class.java)

        itfViewModel.getMedicoById(medicoId).observe(viewLifecycleOwner, {
            if (it != null) {
                m = it
                editTextIdentificador.setText(it.nombreIdentificador)
                editTextIdentificador.isEnabled = false
                editTextCMP.setText(it.cpmMedico)
                editTextCMP.isEnabled = false
                editTextNombre.setText(it.nombreMedico)
                editTextApellidoP.setText(it.apellidoP)
                editTextApellidoM.setText(it.apellidoM)
                editTextCategoria.setText(it.nombreCategoria)
                editTextEspecialidad.setText(it.nombreEspecialidad)
                editTextEspecialidad2.setText(it.nombreEspecialidad2)
                editTextFechaN.setText(it.fechaNacimiento)
                editTextSexo.setText(if (it.sexo == "M") "Masculino" else "Femenino")
                editTextEmail.setText(it.email)
                editTextTelefono.setText(it.telefono)
            } else {
                m.estado = 10
            }
        })

        itfViewModel.getSolMedicoCab(solMedicoId).observe(viewLifecycleOwner, {
            if (it != null) {
                s = it
            }
        })

        editTextIdentificador.setOnClickListener(this)
        editTextCategoria.setOnClickListener(this)
        editTextEspecialidad.setOnClickListener(this)
        editTextEspecialidad2.setOnClickListener(this)
        editTextFechaN.setOnClickListener(this)
        editTextSexo.setOnClickListener(this)
        fabGenerate.setOnClickListener(this)

        if (tipoEnvio == 0) {
            if (tipoMedico == 1) {
                if (estado == 12 || estado == 13 || estado == 100) {
                    fabGenerate.visibility = View.GONE
                }
            }
        }

        itfViewModel.mensajeError.observe(viewLifecycleOwner, {
            Util.toastMensaje(context!!, it)
        })
        itfViewModel.mensajeSuccess.observe(viewLifecycleOwner, {
            if (it == "Ok") {
                formValidateMedico()
                return@observe
            }
            viewPager?.currentItem = 1
            Util.toastMensaje(context!!, it)
        })
    }

    private fun formValidateMedico() {
        m.medicoId = medicoId
        m.medicoSolId = solMedicoId
        m.usuarioId = usuarioId
        m.cpmMedico = editTextCMP.text.toString()
        m.nombreMedico = editTextNombre.text.toString()
        m.apellidoP = editTextApellidoP.text.toString()
        m.apellidoM = editTextApellidoM.text.toString()
        m.nombreIdentificador = editTextIdentificador.text.toString()
        m.nombreCategoria = editTextCategoria.text.toString()
        m.nombreEspecialidad = editTextEspecialidad.text.toString()
        m.nombreEspecialidad2 = editTextEspecialidad2.text.toString()
        m.fechaNacimiento = editTextFechaN.text.toString()
        m.email = editTextEmail.text.toString()
        m.telefono = editTextTelefono.text.toString()
        m.active = 2
        itfViewModel.validateMedico(m)
    }

    private fun formGenerateCabecera() {
        s.usuario = String.format(
            "%s - %s %s %s",
            editTextCMP.text.toString(), editTextNombre.text.toString(),
            editTextApellidoP.text.toString(), editTextApellidoM.text.toString()
        )
        s.estadoSol = 11
        s.solMedicoId = solMedicoId
        s.usuarioId = usuarioId
        s.fecha = Util.getFecha()
        s.tipo = tipoMedico
        itfViewModel.insertSolMedicoInit(s)
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
                val identificadorAdapter =
                    IdentificadorAdapter(object : OnItemClickListener.IdentificadorListener {
                        override fun onItemClick(i: Identificador, view: View, position: Int) {
                            m.identificadorId = i.identificadorId
                            editTextIdentificador.setText(i.descripcion)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = identificadorAdapter

                itfViewModel.getIdentificadores().observe(this, {
                    if (it.isNullOrEmpty()) {
                        itfViewModel.setError("Datos vacios favor de sincronizar")
                    }
                    identificadorAdapter.addItems(it)
                })
            }
            2 -> {
                val categoriaAdapter =
                    ComboCategoriaAdapter(object : OnItemClickListener.CategoriaListener {
                        override fun onItemClick(c: Categoria, view: View, position: Int) {
                            m.categoriaId = c.categoriaId
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
            3, 4 -> {
                val especialidaAdapter =
                    ComboEspecialidadAdapter(object : OnItemClickListener.EspecialidadListener {
                        override fun onItemClick(e: Especialidad, view: View, position: Int) {
                            when (tipo) {
                                3 -> {
                                    m.especialidadId = e.especialidadId
                                    editTextEspecialidad.setText(e.descripcion)
                                }
                                else -> {
                                    m.especialidadId2 = e.especialidadId
                                    editTextEspecialidad2.setText(e.descripcion)
                                }
                            }
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = especialidaAdapter
                itfViewModel.getEspecialidades().observe(this, {
                    if (it.isNullOrEmpty()) {
                        itfViewModel.setError("Datos vacios favor de sincronizar")
                    }
                    especialidaAdapter.addItems(it)
                })
            }
            5 -> {
                val comboAdapter = ComboAdapter(object : OnItemClickListener.ComboListener {
                    override fun onItemClick(c: Combos, view: View, position: Int) {
                        m.sexo = c.abrev
                        editTextSexo.setText(c.title)
                        dialog.dismiss()
                    }
                })
                recyclerView.adapter = comboAdapter
                val a = ArrayList<Combos>()
                a.add(Combos(1, "Masculino", "M"))
                a.add(Combos(0, "Femenino", "F"))
                comboAdapter.addItems(a)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int) =
            GeneralFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, p1)
                    putInt(ARG_PARAM2, p2)
                    putInt(ARG_PARAM3, p3)
                    putInt(ARG_PARAM4, p4)
                    putInt(ARG_PARAM5, p5)
                    putInt(ARG_PARAM6, p6)
                }
            }
    }
}