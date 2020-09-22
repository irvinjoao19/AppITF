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
import com.google.android.material.checkbox.MaterialCheckBox
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Personal
import com.itfperu.appitf.data.local.model.Combos
import com.itfperu.appitf.data.local.model.Perfil
import com.itfperu.appitf.data.viewModel.PerfilViewModel
import com.itfperu.appitf.data.viewModel.PersonalViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.adapters.ComboAdapter
import com.itfperu.appitf.ui.adapters.ComboPerfilAdapter
import com.itfperu.appitf.ui.adapters.ComboPersonalAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_edit_personal.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditPersonalFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        if (v is MaterialCheckBox) {
            val checked: Boolean = v.isChecked
            if (checked) {
                editTextSupervisor.isEnabled = false
            } else {
                p.supervisorId = 0
                editTextSupervisor.text = null
            }

            return
        }

        when (v.id) {
            R.id.editTextRol -> spinnerDialog(3, "Rol")
            R.id.editTextFechaN -> Util.getDateDialog(context!!, editTextFechaN)
            R.id.editTextSexo -> spinnerDialog(2, "Sexo")
            R.id.editTextSupervisor -> spinnerDialog(4, "Supervisores")
            R.id.editTextEstado -> spinnerDialog(1, "Estado")
            R.id.fabGenerate -> formPersonal()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: PersonalViewModel
    lateinit var perfilViewModel: PerfilViewModel
    lateinit var p: Personal
    private var usuarioId: Int = 0
    private var personalId: Int = 0
    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        p = Personal()
        arguments?.let {
            personalId = it.getInt(ARG_PARAM1)
            usuarioId = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_personal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(PersonalViewModel::class.java)
        perfilViewModel =
            ViewModelProvider(this, viewModelFactory).get(PerfilViewModel::class.java)

        p.estado = 1
        editTextEstado.setText(String.format("ACTIVO"))

        itfViewModel.getPersonalById(personalId).observe(viewLifecycleOwner, {
            if (it != null) {
                p = it
                editTextCodigo.setText(it.login)
                editTextCodigo.isEnabled = false
                editTextRol.setText(it.nombrePerfil)
                editTextNombre.setText(it.nombre)
                editTextApellidoP.setText(it.apellidoP)
                editTextApellidoM.setText(it.apellidoM)
                editTextDni.setText(it.nroDoc)
                editTextFechaN.setText(it.fechaNacimiento)
                editTextSexo.setText(if (it.sexo == "M") "Masculino" else "Femenino")
                editTextCelular.setText(it.celular)
                editTextEmail.setText(it.email)
                if (it.esSupervisorId == 1) {
                    editTextSupervisor.isEnabled = false
                }
                checkSupervisor.isChecked = it.esSupervisorId == 1
                editTextSupervisor.setText(it.nombreSupervisor)
                editTextEstado.setText(it.nombreEstado)
            }
        })

        fabGenerate.setOnClickListener(this)
        editTextRol.setOnClickListener(this)
        editTextFechaN.setOnClickListener(this)
        editTextSexo.setOnClickListener(this)
        editTextSupervisor.setOnClickListener(this)
        editTextEstado.setOnClickListener(this)
        checkSupervisor.setOnClickListener(this)

        itfViewModel.mensajeError.observe(viewLifecycleOwner, {
            closeLoad()
            Util.toastMensaje(context!!, it)
        })

        itfViewModel.mensajeSuccess.observe(viewLifecycleOwner, {
            closeLoad()
            Util.toastMensaje(context!!, it)
            activity!!.finish()
        })
    }

    private fun formPersonal() {
        p.login = editTextCodigo.text.toString()
        p.nombrePerfil = editTextRol.text.toString()
        p.nombre = editTextNombre.text.toString()
        p.apellidoP = editTextApellidoP.text.toString()
        p.apellidoM = editTextApellidoM.text.toString()
        p.nroDoc = editTextDni.text.toString()
        p.fechaNacimiento = editTextFechaN.text.toString()
        p.celular = editTextCelular.text.toString()
        p.email = editTextEmail.text.toString()
        p.pass = editTextPass.text.toString()
        p.esSupervisorId = if (checkSupervisor.isChecked) 1 else 0
        p.nombreSupervisor = editTextSupervisor.text.toString()
        p.nombreEstado = editTextEstado.text.toString()
        p.usuarioId = usuarioId
        load()
        itfViewModel.validatePersonal(p)
    }

    private fun load() {
        builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(context).inflate(R.layout.dialog_login, null)
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
            3 -> {
                perfilViewModel.syncPerfil()
                val perfilAdapter =
                    ComboPerfilAdapter(object : OnItemClickListener.PerfilListener {
                        override fun onItemClick(p: Perfil, view: View, position: Int) {
                            p.perfilId = p.perfilId
                            editTextRol.setText(p.descripcion)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = perfilAdapter

                perfilViewModel.getPerfils().observe(this, {
                    progressBar.visibility = View.GONE
                    perfilAdapter.addItems(it)
                })
            }
            4 -> {
                val personalAdapter =
                    ComboPersonalAdapter(object : OnItemClickListener.PersonalListener {
                        override fun onItemClick(p: Personal, view: View, position: Int) {
                            p.supervisorId = p.personalId
                            editTextSupervisor.setText(
                                String.format(
                                    "%s %s %s",
                                    p.nombre, p.apellidoP, p.apellidoM
                                )
                            )
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = personalAdapter
                itfViewModel.getSupervisores().observe(this, {
                    personalAdapter.addItems(it)
                })
            }
            else -> {
                val estadoAdapter = ComboAdapter(object : OnItemClickListener.ComboListener {
                    override fun onItemClick(c: Combos, view: View, position: Int) {
                        when (tipo) {
                            1 -> {
                                p.estado = c.id
                                editTextEstado.setText(c.title)
                            }
                            2 -> {
                                p.sexo = c.abrev
                                editTextSexo.setText(c.title)
                            }
                        }
                        dialog.dismiss()
                    }
                })
                recyclerView.adapter = estadoAdapter
                val a = ArrayList<Combos>()
                when (tipo) {
                    1 -> {
                        a.add(Combos(1, "ACTIVO"))
                        a.add(Combos(0, "INACTIVO"))
                    }
                    2 -> {
                        a.add(Combos(1, "Masculino", "M"))
                        a.add(Combos(0, "Femenino", "F"))
                    }
                }
                estadoAdapter.addItems(a)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            EditPersonalFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}