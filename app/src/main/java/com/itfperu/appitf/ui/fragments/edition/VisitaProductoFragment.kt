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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.ProgramacionDet
import com.itfperu.appitf.data.local.model.Stock
import com.itfperu.appitf.data.viewModel.ProgramacionViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.adapters.ComboStockAdapter
import com.itfperu.appitf.ui.adapters.ProgramacionDetAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.cardview_programacion_det.view.*
import kotlinx.android.synthetic.main.fragment_visita_producto.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class VisitaProductoFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabProducto -> if (validacion != 0) {
                dialogProducto(0)
            } else
                itfViewModel.setError("Completar el primer formulario.")
            R.id.fabSave -> itfViewModel.closeProgramacion(programacionId)
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: ProgramacionViewModel
    private var usuarioId: Int = 0
    private var programacionId: Int = 0
    private var validacion: Int = 0
    private var estado: Int = 0 // si el estado es diferente de 13 no se necesita agregar producto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            programacionId = it.getInt(ARG_PARAM1)
            usuarioId = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_visita_producto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(ProgramacionViewModel::class.java)

        itfViewModel.getProgramacionById(programacionId).observe(viewLifecycleOwner, {
            if (it != null) {
                validacion = it.active
                estado = it.resultadoVisitaId
                if (it.resultadoVisitaId != 13) {
                    if (it.active != 0) {
                        fabSave.visibility = View.VISIBLE
                    } else {
                        fabSave.visibility = View.GONE
                    }
                } else {
                    fabSave.visibility = View.GONE
                }
            }
        })

        val productoAdapter =
            ProgramacionDetAdapter(object : OnItemClickListener.ProgramacionDetListener {
                override fun onItemClick(p: ProgramacionDet, view: View, position: Int) {
                    when (view.id) {
                        R.id.imgDelete -> confirmDelete(p)
                        else -> dialogProducto(p.programacionDetId)
                    }
                }
            })
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = productoAdapter
        itfViewModel.getProgramacionesDetById(programacionId).observe(viewLifecycleOwner, {
            if (estado == 13) {
                if (it.isNotEmpty()) {
                    fabSave.visibility = View.VISIBLE
                } else
                    fabSave.visibility = View.GONE
            }
            productoAdapter.addItems(it)
        })

        itfViewModel.mensajeError.observe(viewLifecycleOwner, {
            Util.toastMensaje(context!!, it)
        })
        itfViewModel.mensajeSuccess.observe(viewLifecycleOwner, {
            Util.toastMensaje(context!!, it)
            if (it == "Guardado") {
                activity!!.finish()
            }
        })
        fabProducto.setOnClickListener(this)
        fabSave.setOnClickListener(this)
    }

    private fun dialogProducto(id: Int) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_producto, null)
        val layoutTitle: TextView = v.findViewById(R.id.layoutTitle)
        val editTextProducto: TextInputEditText = v.findViewById(R.id.editTextProducto)
        val editTextLote: TextInputEditText = v.findViewById(R.id.editTextLote)
        val editTextStock: TextInputEditText = v.findViewById(R.id.editTextStock)
        val editTextCantidad: TextInputEditText = v.findViewById(R.id.editTextCantidad)
        val editTextOrden: TextInputEditText = v.findViewById(R.id.editTextOrden)
        val fabProducto: ExtendedFloatingActionButton = v.findViewById(R.id.fabProducto)
        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        var m = ProgramacionDet()
        layoutTitle.text = String.format("Visita Producto")

        itfViewModel.setErrorProducto(null)
        itfViewModel.getProgramacionDetById(id).observe(viewLifecycleOwner, {
            if (it != null) {
                m = it
                editTextProducto.setText(it.descripcionProducto)
                editTextLote.setText(it.lote)
                editTextStock.setText(it.stock.toString())
                editTextCantidad.setText(it.cantidad.toString())
                editTextOrden.setText(it.ordenProgramacion.toString())
                if (it.active == 0){
                    fabProducto.visibility = View.INVISIBLE
                }
            }
        })

        editTextProducto.setOnClickListener {
            spinnerDialog(m, v)
        }

        fabProducto.setOnClickListener {
            m.descripcionProducto = editTextProducto.text.toString()
            m.programacionId = programacionId
            m.lote = editTextLote.text.toString()
            when {
                editTextStock.text.toString().isEmpty() -> m.stock = 0
                else -> m.stock = editTextStock.text.toString().toInt()
            }
            when {
                editTextCantidad.text.toString().isEmpty() -> m.cantidad = 0
                else -> m.cantidad = editTextCantidad.text.toString().toInt()
            }
            when {
                editTextOrden.text.toString().isEmpty() -> m.ordenProgramacion = 0
                else -> m.ordenProgramacion = editTextOrden.text.toString().toInt()
            }
            m.active = 1
            itfViewModel.validateProgramacionDet(m)
        }

        itfViewModel.mensajeProducto.observe(viewLifecycleOwner, {
            if (it != null) {
                dialog.dismiss()
            }
        })
    }

    private fun spinnerDialog(
        p: ProgramacionDet, vista: View
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
        textViewTitulo.text = String.format("Productos")

        val editTextProducto: TextInputEditText = vista.findViewById(R.id.editTextProducto)
        val editTextLote: TextInputEditText = vista.findViewById(R.id.editTextLote)
        val editTextStock: TextInputEditText = vista.findViewById(R.id.editTextStock)

        val stockAdapter =
            ComboStockAdapter(object : OnItemClickListener.StockListener {
                override fun onItemClick(s: Stock, view: View, position: Int) {
                    p.productoId = s.productoId
                    p.codigoProducto = s.codigoProducto
                    editTextProducto.setText(s.descripcion)
                    editTextLote.setText(s.lote)
                    editTextStock.setText(s.stock.toString())
                    dialog.dismiss()
                }
            })
        recyclerView.adapter = stockAdapter
        itfViewModel.getStocks().observe(this, {
            if (it.isNullOrEmpty()) {
                itfViewModel.setError("Datos vacios favor de sincronizar")
            }
            stockAdapter.addItems(it)
        })
    }

    private fun confirmDelete(p: ProgramacionDet) {
        val dialog = MaterialAlertDialogBuilder(context!!)
            .setTitle("Mensaje")
            .setMessage("Deseas eliminar este producto ?")
            .setPositiveButton("SI") { dialog, _ ->
                itfViewModel.deleteProgramacionDet(p)
                dialog.dismiss()
            }
            .setNegativeButton("NO") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            VisitaProductoFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}