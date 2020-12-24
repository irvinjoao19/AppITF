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
import com.itfperu.appitf.data.local.model.Ciclo
import com.itfperu.appitf.data.local.model.Combos
import com.itfperu.appitf.data.viewModel.CicloViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.adapters.ComboAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_edit_ciclo.*
import javax.inject.Inject
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditCicloFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.editTextDesde -> Util.getDateDialog(requireContext(), editTextDesde)
            R.id.editTextHasta -> Util.getDateDialog(requireContext(), editTextHasta)
            R.id.editTextEstado -> spinnerDialog()
            R.id.fabGenerate -> formCiclo()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: CicloViewModel
    lateinit var p: Ciclo
    private var usuarioId: Int = 0
    private var cicloId: Int = 0
    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        p = Ciclo()
        arguments?.let {
            cicloId = it.getInt(ARG_PARAM1)
            usuarioId = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_ciclo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(CicloViewModel::class.java)

        p.estado = 3
        editTextEstado.setText(String.format("Activo"))

        itfViewModel.getCicloById(cicloId).observe(viewLifecycleOwner, {
            if (it != null) {
                p = it
                editTextDescripcion.setText(it.nombre)
                editTextDesde.setText(it.desde)
                editTextHasta.setText(it.hasta)
                editTextEstado.setText(it.nombreEstado)
            }
        })

        fabGenerate.setOnClickListener(this)
        editTextEstado.setOnClickListener(this)
        editTextDesde.setOnClickListener(this)
        editTextHasta.setOnClickListener(this)

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

    private fun formCiclo() {
        p.nombre = editTextDescripcion.text.toString()
        p.desde = editTextDesde.text.toString()
        p.hasta = editTextHasta.text.toString()
        p.nombreEstado = editTextEstado.text.toString()
        p.usuarioId = usuarioId
        load()
        itfViewModel.validateCiclo(p)
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
                p.estado = c.id
                editTextEstado.setText(c.title)
                dialog.dismiss()
            }
        })
        recyclerView.adapter = estadoAdapter

        val a = ArrayList<Combos>()
        a.add(Combos(3, "Activo"))
        a.add(Combos(4, "En Proceso"))
        a.add(Combos(5, "Cerrado"))
        estadoAdapter.addItems(a)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            EditCicloFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}