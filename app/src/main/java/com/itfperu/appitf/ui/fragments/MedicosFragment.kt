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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.*
import com.itfperu.appitf.data.viewModel.MedicoViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.activities.FormActivity
import com.itfperu.appitf.ui.activities.MedicoActivity
import com.itfperu.appitf.ui.adapters.SolMedicoAdapter
import com.itfperu.appitf.ui.adapters.ComboEstadoAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_medicos.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MedicosFragment : DaggerFragment(), SwipeRefreshLayout.OnRefreshListener,
    View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabAdd -> startActivity(
                Intent(context, MedicoActivity::class.java)
                    .putExtra("usuarioId", usuarioId)
                    .putExtra("solMedicoId", solMedicoId)
                    .putExtra("medicoId", medicoId)
                    .putExtra("title", "Nuevo Medico")
                    .putExtra("tipoMedico", tipoMedico)
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
    lateinit var itfViewModel: MedicoViewModel
    lateinit var adapterSol: SolMedicoAdapter
    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null
    private var usuarioId: Int = 0
    private var tipo: Int = 14
    private var tipoMedico: Int = 0 // 1 -> actividades , 2 -> aprobadas

    private var finicio: String = Util.getFirstDay()
    private var ffinal: String = Util.getLastaDay()
    private var estado: Int = 0
    private var medicoId: Int = 0 // medico
    private var solMedicoId: Int = 0 // medico cab

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            usuarioId = it.getInt(ARG_PARAM1)
            tipoMedico = it.getInt(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_medicos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(MedicoViewModel::class.java)

        adapterSol = SolMedicoAdapter(object : OnItemClickListener.SolMedicoListener {
            override fun onItemClick(m: SolMedico, view: View, position: Int) {
                val title = when (tipoMedico) {
                    1 -> "Modificar Medico"
                    else -> "Aprobar o Rechazar Medico"
                }

                startActivity(
                    Intent(context, FormActivity::class.java)
                        .putExtra("title", title)
                        .putExtra("tipo", tipo)
                        .putExtra("id", m.solMedicoId)
                        .putExtra("uId", m.usuarioId)
                        .putExtra("tipoMedico", m.tipo)
                        .putExtra("estado", m.estadoSol)
                )
            }
        })

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapterSol

        itfViewModel.setLoading(true)
        itfViewModel.syncSolMedico(usuarioId, finicio, ffinal, 0, tipoMedico)

        refreshLayout.setOnRefreshListener(this)
        fabAdd.setOnClickListener(this)
        fabSave.setOnClickListener(this)

        if (tipoMedico == 2) {
            fabAdd.title = "Enviar Aprobaciones"
            fabAdd.visibility = View.GONE
        }

        itfViewModel.getSolMedicos(tipoMedico).observe(viewLifecycleOwner, {
//            textviewMessage.text = String.format("Se encontraron %s registros", it.size)
            refreshLayout.isRefreshing = false
            adapterSol.addItems(it)
        })

        itfViewModel.getSolMedicoId().observe(viewLifecycleOwner, {
            solMedicoId = if (it == null || it == 0) 1 else it + 1
        })

        itfViewModel.getMedicoId().observe(viewLifecycleOwner, {
            medicoId = if (it == null || it == 0) 1 else it + 1
        })

        itfViewModel.loading.observe(viewLifecycleOwner, {
            if (it) {
                adapterSol.addItems(listOfNotNull())
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
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

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            MedicosFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }

    override fun onRefresh() {
        itfViewModel.setLoading(false)
        itfViewModel.syncSolMedico(usuarioId, finicio, ffinal, 0, tipoMedico)
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
            itfViewModel.syncSolMedico(
                usuarioId, finicio, ffinal, estado, tipoMedico
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
        itfViewModel.getEstados("tbl_Sol_Medico_Cab").observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) {
                itfViewModel.setError("Datos vacios favor de sincronizar Visita Medica")
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
                itfViewModel.sendMedicos(tipoMedico)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
    }
}