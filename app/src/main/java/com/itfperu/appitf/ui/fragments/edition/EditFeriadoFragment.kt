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
import com.itfperu.appitf.data.local.model.Combos
import com.itfperu.appitf.data.local.model.Feriado
import com.itfperu.appitf.data.viewModel.FeriadoViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.adapters.ComboAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_edit_feriado.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditFeriadoFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.editTextFecha -> Util.getDateDialog(requireContext(), editTextFecha)
            R.id.editTextEstado -> spinnerDialog()
            R.id.fabGenerate -> formFeriado()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: FeriadoViewModel
    lateinit var p: Feriado
    private var usuarioId: Int = 0
    private var feriadoId: Int = 0
    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        p = Feriado()
        arguments?.let {
            feriadoId = it.getInt(ARG_PARAM1)
            usuarioId = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_feriado, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(FeriadoViewModel::class.java)

        p.estadoId = 1
        editTextEstado.setText(String.format("ACTIVO"))
        itfViewModel.getFeriadoById(feriadoId).observe(viewLifecycleOwner, {
            if (it != null) {
                p = it
                editTextNombre.setText(it.descripcion)
                editTextFecha.setText(it.fecha)
                editTextFecha.isEnabled = false
                editTextEstado.setText(it.estado)
            }
        })

        fabGenerate.setOnClickListener(this)
        editTextEstado.setOnClickListener(this)
        editTextFecha.setOnClickListener(this)

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

    private fun formFeriado() {
        p.fecha = editTextFecha.text.toString()
        p.descripcion = editTextNombre.text.toString()
        p.estado = editTextEstado.text.toString()
        p.usuarioId = usuarioId
        load()
        itfViewModel.validateFeriado(p)
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

    private fun spinnerDialog() {
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
        textViewTitulo.text = String.format("Estado")

        val estadoAdapter = ComboAdapter(object : OnItemClickListener.ComboListener {
            override fun onItemClick(c: Combos, view: View, position: Int) {
                p.estadoId = c.id
                editTextEstado.setText(c.title)
                dialog.dismiss()
            }
        })
        recyclerView.adapter = estadoAdapter

        val a = ArrayList<Combos>()
        a.add(Combos(1, "ACTIVO"))
        a.add(Combos(0, "INACTIVO"))
        estadoAdapter.addItems(a)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            EditFeriadoFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}