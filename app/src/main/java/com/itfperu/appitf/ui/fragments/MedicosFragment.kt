package com.itfperu.appitf.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
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
    lateinit var f: Filtro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            usuarioId = it.getInt(ARG_PARAM1)
            tipoMedico = it.getInt(ARG_PARAM2)
        }

        f = Filtro(
            if (tipoMedico == 1) usuarioId else 0,
            finicio, ffinal,
            0,
            tipoMedico
        )

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
                if (m.estado == 3) {
                    val popupMenu = PopupMenu(context!!, view)
                    popupMenu.menu.add(0, 1, 0, getText(R.string.ver))
                    popupMenu.menu.add(0, 2, 0, getText(R.string.delete))
                    popupMenu.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            1 -> goActivity(m)
                            2 -> deleteSolMedico(m)
                        }
                        false
                    }
                    popupMenu.show()
                    return
                }

                val title = when (tipoMedico) {
                    1 -> "Modificar Médico"
                    else -> "Aprobar o Rechazar Médico"
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
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapterSol

        itfViewModel.setLoading(true)
        itfViewModel.syncSolMedico(
            if (tipoMedico == 1) usuarioId else 0,
            finicio, ffinal,
            if (tipoMedico == 1) 0 else 11,
            tipoMedico
        )
        itfViewModel.search.value = Gson().toJson(f)

        if (tipoMedico == 2) {
            fabAdd.title = "Enviar Aprobaciones"
            fabAdd.visibility = View.GONE
        }
        itfViewModel.getMedicos().observe(viewLifecycleOwner, {
            textviewMessage.text = String.format("Se encontraron %s registros", it.size)
            //refreshLayout.isRefreshing = false
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
            Util.toastMensaje(requireContext(), it)
        })
        itfViewModel.mensajeError.observe(viewLifecycleOwner, {
            closeLoad()
            Util.toastMensaje(requireContext(), it)
        })
        fabAdd.setOnClickListener(this)
        fabSave.setOnClickListener(this)
        //refreshLayout.setOnRefreshListener(this)
    }

    private fun deleteSolMedico(m: SolMedico) {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Mensaje")
            .setMessage(String.format("Deseas eliminar formulario incompleto ?."))
            .setPositiveButton("Si") { dialog, _ ->
                itfViewModel.deleteSolMedico(m)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
    }

    private fun goActivity(m: SolMedico) {
        val title = when (tipoMedico) {
            1 -> "Modificar Médico"
            else -> "Aprobar o Rechazar Médico"
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

        editTextDesde.setOnClickListener { Util.getDateDialog(requireContext(), editTextDesde) }
        editTextHasta.setOnClickListener { Util.getDateDialog(requireContext(), editTextHasta) }
        editTextEstado.setOnClickListener { spinnerDialog(editTextEstado, f) }
        fabSearch.setOnClickListener {
            f.finicio = editTextDesde.text.toString()
            f.ffinal = editTextHasta.text.toString()
            itfViewModel.setLoading(true)
            itfViewModel.syncSolMedico(
                if (tipoMedico == 1) usuarioId else 0,
                f.finicio, f.ffinal,
                f.estadoId,
                tipoMedico
            )

            val search = Filtro(
                if (tipoMedico == 1) usuarioId else 0, f.finicio, f.ffinal, f.estadoId, tipoMedico
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
        itfViewModel.getEstados("tbl_Sol_Medico_Cab").observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) {
                itfViewModel.setError("Datos vacios favor de sincronizar Visita Medica")
            }
            estadoAdapter.addItems(it)
        })
    }

    private fun confirmSend() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
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