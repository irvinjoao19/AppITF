package com.itfperu.appitf.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
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
import com.itfperu.appitf.data.local.model.*
import com.itfperu.appitf.data.viewModel.TargetViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.activities.FormActivity
import com.itfperu.appitf.ui.adapters.*
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_target_altas.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

class TargetAltasFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabAdd -> startActivity(
                Intent(context, FormActivity::class.java)
                    .putExtra("title", "Nueva Target")
                    .putExtra(
                        "tipo", when (tipo) {
                            1 -> 15
                            else -> 16
                        }
                    )
                    .putExtra("tipoTarget", tipoTarget)
                    .putExtra("id", targetCabId)
                    .putExtra("uId", usuarioId)
            )
            R.id.fabSave -> confirmSend()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.search).setVisible(true).isEnabled = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> dialogSearch()
        }
        return super.onOptionsItemSelected(item)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: TargetViewModel
    lateinit var adapter: TargetAltasAdapter
    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null

    private var usuarioId: Int = 0

    private var tipoTarget: String = "" // A -> ALTAS	B -> BAJAS
    private var tipo: Int = 0 // 1 -> altas , 2 -> aprobacion de altas
    private var finicio: String = Util.getFirstDay()
    private var ffinal: String = Util.getLastaDay()
    private var estado: Int = 0
    private var targetCabId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            usuarioId = it.getInt(ARG_PARAM1)
            tipoTarget = it.getString(ARG_PARAM2)!!
            tipo = it.getInt(ARG_PARAM3)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_target_altas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(TargetViewModel::class.java)

        adapter = TargetAltasAdapter(object : OnItemClickListener.TargetAltasListener {
            override fun onItemClick(t: TargetCab, view: View, position: Int) {
                startActivity(
                    Intent(context, FormActivity::class.java)
                        .putExtra(
                            "title", if (tipoTarget == "A") {
                                "Editar Altas"
                            } else {
                                "Editar Bajas"
                            }
                        )
                        .putExtra("tipo", 15)
                        .putExtra("tipoTarget", t.tipoTarget)
                        .putExtra("id", t.targetCabId)
                        .putExtra("uId", t.usuarioId)
                )
            }
        })

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        itfViewModel.setLoading(true)
        itfViewModel.syncTargetCab(usuarioId, finicio, ffinal, 0, tipoTarget, tipo)

        fabAdd.setOnClickListener(this)
        fabSave.setOnClickListener(this)

        if (tipoTarget == "A") {
            fabAdd.title = "Nuevas Altas"
        } else {
            fabAdd.title = "Nuevas Bajas"
        }

        if (tipo == 2) {
            fabAdd.visibility = View.GONE
        }

        itfViewModel.getTargetsAltas(tipoTarget).observe(viewLifecycleOwner, {
//            textviewMessage.text = String.format("Se encontraron %s registros", it.size)
            adapter.addItems(it)
        })

        itfViewModel.loading.observe(viewLifecycleOwner, {
            if (it) {
                adapter.addItems(listOfNotNull())
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })

        itfViewModel.getTargetCabId().observe(viewLifecycleOwner, {
            targetCabId = if (it == null || it == 0) 1 else it + 1
        })

        itfViewModel.mensajeSuccess.observe(viewLifecycleOwner, {
            closeLoad()
            Util.toastMensaje(context!!, it)
        })

        itfViewModel.mensajeError.observe(viewLifecycleOwner, {
            closeLoad()
            Util.toastMensaje(context!!, it)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: String, param3: Int) =
            TargetAltasFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putInt(ARG_PARAM3, param3)
                }
            }
    }

    private fun dialogSearch() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_filtro_medico, null)

        val editTextDesde: TextInputEditText = v.findViewById(R.id.editTextDesde)
        val editTextHasta: TextInputEditText = v.findViewById(R.id.editTextHasta)
        val editTextEstado: TextInputEditText = v.findViewById(R.id.editTextEstado)
        val fabSearch: ExtendedFloatingActionButton = v.findViewById(R.id.fabSearch)

        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        val f = Filtro()

        editTextDesde.setOnClickListener { Util.getDateDialog(context!!, editTextDesde) }
        editTextHasta.setOnClickListener { Util.getDateDialog(context!!, editTextHasta) }
        editTextEstado.setOnClickListener { spinnerDialog(editTextEstado, f) }
        fabSearch.setOnClickListener {
            finicio = editTextDesde.text.toString()
            ffinal = editTextHasta.text.toString()
            itfViewModel.setLoading(true)
            itfViewModel.syncTargetCab(
                usuarioId, finicio, ffinal, estado, tipoTarget, tipo
            )
            dialog.dismiss()
        }
    }

    private fun spinnerDialog(input: TextInputEditText, f: Filtro) {
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
        textViewTitulo.text = String.format("Estado")

        val estadoAdapter = ComboEstadoAdapter(object : OnItemClickListener.EstadoListener {
            override fun onItemClick(e: Estado, view: View, position: Int) {
                estado = e.estadoId
                input.setText(e.descripcion)
                dialog.dismiss()
            }
        })
        recyclerView.adapter = estadoAdapter
        itfViewModel.getEstados("tbl_Target_Cab").observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) {
                itfViewModel.setError("Datos vacios favor de sincronizar")
            }
            estadoAdapter.addItems(it)
        })
    }

    private fun confirmSend() {
        val dialog = MaterialAlertDialogBuilder(context!!)
            .setTitle("Mensaje")
            .setMessage(String.format("Deseas enviar las solicitudes ?."))
            .setPositiveButton("Si") { dialog, _ ->
                load()
                itfViewModel.sendTarges(tipoTarget)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
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
}