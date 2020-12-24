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
import com.itfperu.appitf.data.local.model.Control
import com.itfperu.appitf.data.local.model.Producto
import com.itfperu.appitf.data.local.model.TipoProducto
import com.itfperu.appitf.data.viewModel.ProductoViewModel
import com.itfperu.appitf.data.viewModel.TipoProductoViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.adapters.ComboAdapter
import com.itfperu.appitf.ui.adapters.ComboTipoProductoAdapter
import com.itfperu.appitf.ui.adapters.ControlAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_edit_producto.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditProductoFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.editTextEstado -> spinnerDialog(1, "Estado")
            R.id.editTextTipo -> spinnerDialog(2, "Tipo de Producto")
            R.id.editTextControl -> spinnerDialog(3, "Control")
            R.id.fabGenerate -> formProducto()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: ProductoViewModel
    lateinit var tipoProductoViewModel: TipoProductoViewModel
    lateinit var p: Producto
    private var usuarioId: Int = 0
    private var productoId: Int = 0
    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        p = Producto()
        arguments?.let {
            productoId = it.getInt(ARG_PARAM1)
            usuarioId = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_producto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(ProductoViewModel::class.java)
        tipoProductoViewModel =
            ViewModelProvider(this, viewModelFactory).get(TipoProductoViewModel::class.java)

        p.estadoId = 1
        editTextEstado.setText(String.format("ACTIVO"))
        itfViewModel.getProductoById(productoId).observe(viewLifecycleOwner, {
            if (it != null) {
                p = it
                editTextCodigo.setText(it.codigo)
                editTextCodigo.isEnabled = false
                editTextNombre.setText(it.descripcion)
                editTextTipo.setText(it.descripcionTipoProducto)
                editTextAbreviatura.setText(it.abreviatura)
                editTextControl.setText(it.control)
                editTextEstado.setText(it.estado)
            }
        })

        fabGenerate.setOnClickListener(this)
        editTextEstado.setOnClickListener(this)
        editTextTipo.setOnClickListener(this)
        editTextControl.setOnClickListener(this)

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

    private fun formProducto() {
        p.codigo = editTextCodigo.text.toString()
        p.descripcion = editTextNombre.text.toString()
        p.estado = editTextEstado.text.toString()
        p.abreviatura = editTextAbreviatura.text.toString()
        p.descripcionTipoProducto = editTextTipo.text.toString()
        p.control = editTextControl.text.toString()
        p.usuarioId = usuarioId
        load()
        itfViewModel.validateProducto(p)
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
                progressBar.visibility = View.GONE
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
            2 -> {
                tipoProductoViewModel.syncTipoProducto()
                val tipoAdapter =
                    ComboTipoProductoAdapter(object : OnItemClickListener.TipoProductoListener {
                        override fun onItemClick(t: TipoProducto, view: View, position: Int) {
                            p.tipoProductoId = t.tipoProductoId
                            p.tipo = t.codigo
                            editTextTipo.setText(t.descripcion)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = tipoAdapter

                tipoProductoViewModel.getTipoProductoActive().observe(this, {
                    if (it.isNotEmpty()) {
                        progressBar.visibility = View.GONE
                    } else {
                        progressBar.visibility = View.VISIBLE
                    }
                    tipoAdapter.addItems(it)
                })
            }
            3 -> {
                itfViewModel.syncControl()
                val controlAdapter =
                    ControlAdapter(object : OnItemClickListener.ControlListener {
                        override fun onItemClick(c: Control, view: View, position: Int) {
                            p.controlId = c.controlId
                            editTextControl.setText(c.descripcion)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = controlAdapter
                itfViewModel.getControls().observe(this, {
                    if (it.isNotEmpty()) {
                        progressBar.visibility = View.GONE
                    } else {
                        progressBar.visibility = View.VISIBLE
                    }
                    controlAdapter.addItems(it)
                })
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            EditProductoFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}