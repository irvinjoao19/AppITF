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
import com.google.gson.Gson
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Estado
import com.itfperu.appitf.data.local.model.Filtro
import com.itfperu.appitf.data.local.model.NuevaDireccion
import com.itfperu.appitf.data.viewModel.DireccionViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.activities.FormActivity
import com.itfperu.appitf.ui.adapters.ComboEstadoAdapter
import com.itfperu.appitf.ui.adapters.NuevaDireccionAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_direcciones.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DireccionesFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabAdd -> startActivity(
                Intent(context, FormActivity::class.java)
                    .putExtra(
                        "title", when (tipo) {
                            1 -> "Nueva Dirección"
                            else -> "Aprobación Dirección"
                        }
                    )
                    .putExtra(
                        "tipo", when (tipo) {
                            1 -> 16
                            else -> 17
                        }
                    )
                    .putExtra("id", 0)
                    .putExtra("uId", usuarioId)
            )
            R.id.fabSave -> confirmSend()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: DireccionViewModel
    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null
    private var usuarioId: Int = 0
    private var solDireccionId: Int = 0
    private var tipo: Int = 0 // 1 ->nuevo 2 -> aprobaciones
    private var finicio: String = Util.getFirstDay()
    private var ffinal: String = Util.getLastaDay()
    lateinit var f: Filtro

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            usuarioId = it.getInt(ARG_PARAM1)
            tipo = it.getInt(ARG_PARAM2)
        }

        f = Filtro(
            usuarioId, finicio, ffinal, 0, tipo
        )

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_direcciones, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(DireccionViewModel::class.java)

        val direccionAdapter =
            NuevaDireccionAdapter(object : OnItemClickListener.NuevaDireccionListener {
                override fun onItemClick(n: NuevaDireccion, view: View, position: Int) {
//                    if (tipo == 1) {
//                        if (a.estado == 8 || a.estado == 9)
//                            return
//                    }
                    val title = when (tipo) {
                        1 -> "Modificar Nueva Dirección"
                        else -> "Aprobación Nueva Dirección"
                    }
                    val tipo = when (tipo) {
                        1 -> 16
                        else -> 17
                    }
                    startActivity(
                        Intent(context, FormActivity::class.java)
                            .putExtra("title", title)
                            .putExtra("tipo", tipo)
                            .putExtra("id", n.solDireccionId)
                            .putExtra("uId", usuarioId)
                    )
                }
            })

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = direccionAdapter

        itfViewModel.setLoading(true)
        itfViewModel.syncDireccion(usuarioId, finicio, ffinal, 0, tipo)
        itfViewModel.search.value = Gson().toJson(f)

        itfViewModel.getDirecciones().observe(viewLifecycleOwner, {
            textviewMessage.text = String.format("Se encontraron %s registros", it.size)
            direccionAdapter.addItems(it)
        })

        itfViewModel.loading.observe(viewLifecycleOwner, {
            if (it) {
                direccionAdapter.addItems(listOfNotNull())
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })

        itfViewModel.getNuevaDireccionMaxId().observe(viewLifecycleOwner, {
            solDireccionId = if (it == null || it == 0) 1 else it + 1
        })
        itfViewModel.mensajeSuccess.observe(viewLifecycleOwner, {
            closeLoad()
            Util.toastMensaje(context!!, it)
        })
        itfViewModel.mensajeError.observe(viewLifecycleOwner, {
            closeLoad()
            Util.toastMensaje(context!!, it)
        })
        if(tipo == 2){
            fabAdd.visibility = View.GONE
        }
        fabAdd.setOnClickListener(this)
        fabSave.setOnClickListener(this)
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


    private fun dialogSearch() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_filtro_fecha, null)

        val editTextDesde: TextInputEditText = v.findViewById(R.id.editTextDesde)
        val editTextHasta: TextInputEditText = v.findViewById(R.id.editTextHasta)
        val editTextEstado: TextInputEditText = v.findViewById(R.id.editTextEstado)
        val fabSearch: ExtendedFloatingActionButton = v.findViewById(R.id.fabSearch)

        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        f = Filtro()

        editTextDesde.setText(finicio)
        editTextHasta.setText(ffinal)

        editTextDesde.setOnClickListener { Util.getDateDialog(context!!, editTextDesde) }
        editTextHasta.setOnClickListener { Util.getDateDialog(context!!, editTextHasta) }
        editTextEstado.setOnClickListener { spinnerDialog(editTextEstado, f) }
        fabSearch.setOnClickListener {
            f.finicio = editTextDesde.text.toString()
            f.ffinal = editTextHasta.text.toString()
            itfViewModel.setLoading(true)
            itfViewModel.syncDireccion(
                usuarioId,
                f.finicio, f.ffinal,
                f.estadoId,
                tipo
            )

            val search = Filtro(
                usuarioId, f.finicio, f.ffinal, f.estadoId, tipo
            )
            itfViewModel.search.value = Gson().toJson(search)
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
                f.estadoId = e.estadoId
                input.setText(e.descripcion)
                dialog.dismiss()
            }
        })
        recyclerView.adapter = estadoAdapter
        itfViewModel.getEstados("tbl_Sol_Medico_Direccion").observe(viewLifecycleOwner, {
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
                itfViewModel.sendDireccion(tipo)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            DireccionesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}