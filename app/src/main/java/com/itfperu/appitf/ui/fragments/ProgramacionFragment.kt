package com.itfperu.appitf.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.evrencoskun.tableview.TableView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Estado
import com.itfperu.appitf.data.local.model.Filtro
import com.itfperu.appitf.data.local.model.Programacion
import com.itfperu.appitf.data.local.model.ProgramacionPerfil
import com.itfperu.appitf.data.tableview.TablePerfilAdapter
import com.itfperu.appitf.data.tableview.MyTableViewListener
import com.itfperu.appitf.data.tableview.TableDetalleAdapter
import com.itfperu.appitf.data.tableview.TableRejaAdapter
import com.itfperu.appitf.data.viewModel.ProgramacionViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.activities.VisitaActivity
import com.itfperu.appitf.ui.adapters.ComboEstadoAdapter
import com.itfperu.appitf.ui.adapters.ProgramacionAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_programacion.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"

class ProgramacionFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
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
    lateinit var itfViewModel: ProgramacionViewModel
    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null
    private var usuarioId: Int = 0
    private var programacionId: Int = 0
    lateinit var f: Filtro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        f = Filtro()
        arguments?.let {
            usuarioId = it.getInt(ARG_PARAM1)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_programacion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(ProgramacionViewModel::class.java)

        val adapter = ProgramacionAdapter(object : OnItemClickListener.ProgramacionListener {
            override fun onItemClick(p: Programacion, view: View, position: Int) {
                when (view.id) {
                    R.id.textViewPerfil -> dialogPerfil(p)
                    R.id.textViewReja -> dialogReja(p)
                    else -> startActivity(
                        Intent(context, VisitaActivity::class.java)
                            .putExtra("programacionId", p.programacionId)
                            .putExtra("u", usuarioId)
                    )
                }
            }
        })
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        itfViewModel.setLoading(true)
        itfViewModel.syncProgramacion(usuarioId, 0)
        itfViewModel.search.value = null

        itfViewModel.getProgramaciones().observe(viewLifecycleOwner, {
            textviewMessage.text = String.format("Se encontraron %s registros", it.size)
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

        itfViewModel.mensajeSuccess.observe(viewLifecycleOwner, {
            closeLoad()
            Util.toastMensaje(requireContext(), it)
        })

        itfViewModel.mensajeError.observe(viewLifecycleOwner, {
            closeLoad()
            Util.toastMensaje(requireContext(), it)
        })

        itfViewModel.getProgramacionId().observe(viewLifecycleOwner, {
            programacionId = if (it == null || it == 0) 1 else it + 1
        })

        fabSave.setOnClickListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            ProgramacionFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
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
            LayoutInflater.from(context).inflate(R.layout.dialog_filtro_programacion, null)

        val editTextEstado: TextInputEditText = v.findViewById(R.id.editTextEstado)
        val editTextSearch: TextInputEditText = v.findViewById(R.id.editTextSearch)
        val fabSearch: ExtendedFloatingActionButton = v.findViewById(R.id.fabSearch)

        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        f = Filtro()

        editTextEstado.setOnClickListener { spinnerDialog(editTextEstado, f) }
        fabSearch.setOnClickListener {
//            if (f.estadoId == 0) {
//                itfViewModel.setError("Seleccione Estado")
//                return@setOnClickListener
//            }
            f.search = editTextSearch.text.toString()
            itfViewModel.search.value = Gson().toJson(f)
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
        itfViewModel.getEstados("tbl_Programacion_Cab").observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) {
                itfViewModel.setError("Datos vacios favor de sincronizar")
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
                itfViewModel.sendProgramacion()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
    }

    private fun dialogPerfil(p: Programacion) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(context).inflate(R.layout.dialog_perfil, null)
        val linearLayoutLoad: ConstraintLayout = view.findViewById(R.id.linearLayoutLoad)
        val linearLayoutPrincipal: LinearLayout = view.findViewById(R.id.linearLayoutPrincipal)
        val imageViewClose: ImageView = view.findViewById(R.id.imageViewClose)
        val textView1: TextView = view.findViewById(R.id.textView1)
        val textView2: TextView = view.findViewById(R.id.textView2)
        val textView3: TextView = view.findViewById(R.id.textView3)
        val textView4: TextView = view.findViewById(R.id.textView4)
        val tableView: TableView = view.findViewById(R.id.tableView)

        builder.setView(view)
        val dialog = builder.create()
        dialog.show()

        imageViewClose.setOnClickListener {
            dialog.dismiss()
        }

        itfViewModel.setInfo()
        itfViewModel.setPerfiles()
        itfViewModel.syncProgramacionPerfil(p.medicoId)

        val mTableAdapter =
            TablePerfilAdapter(context, object : OnItemClickListener.ProgramacionPerfilListener {
                override fun onItemClick(s: String) {
                    if (!Util.isNumeric(s)) {
                        if (s != "Total") {
                            dialogPerfilDetalle(p.medicoId, s)
                        }
                    }
                }
            })
        tableView.adapter = mTableAdapter
        tableView.tableViewListener = MyTableViewListener(tableView)

        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                itfViewModel.perfiles.observe(this, { p ->
                    if (p != null) {
                        val e: ProgramacionPerfil? = p[0]
                        if (e != null) {
                            Util.getTextStyleHtml(
                                String.format("<strong>Médico</strong> : %s", e.nombreMedico),
                                textView1
                            )
                            Util.getTextStyleHtml(
                                String.format("<strong>Matricula</strong> : %s", e.matricula),
                                textView2
                            )
                            Util.getTextStyleHtml(
                                String.format(
                                    "<strong>Especialidad</strong> : %s", e.especialidad
                                ), textView3
                            )
                            Util.getTextStyleHtml(
                                String.format("<strong>Dirección</strong> : %s", e.direccion),
                                textView4
                            )
                        }
                        mTableAdapter.setUserList(p)
                        tableView.visibility = View.VISIBLE
                        linearLayoutLoad.visibility = View.GONE
                        linearLayoutPrincipal.visibility = View.VISIBLE
                    }
                })
            }, 800)
        }

        itfViewModel.mensajeInfo.observe(viewLifecycleOwner, {
            if (it != null) {
                Util.toastMensaje(requireContext(), it)
                dialog.dismiss()
            }
        })
    }

    private fun dialogPerfilDetalle(medicoId: Int, s: String) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(context).inflate(R.layout.dialog_perfil_detalle, null)
        val tableView: TableView = view.findViewById(R.id.tableView)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)

        builder.setView(view)
        val dialog = builder.create()
        dialog.show()

        progressBar.visibility = View.VISIBLE
        itfViewModel.setRejas()
        itfViewModel.syncProgramacionPerfilDetalle(medicoId, s)

        val mTableAdapter =
            TableDetalleAdapter(context)
        tableView.adapter = mTableAdapter

        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                itfViewModel.detalles.observe(this, { p ->
                    if (p != null) {
                        mTableAdapter.setUserList(p)
                        progressBar.visibility = View.GONE
                    }
                })
            }, 800)
        }
    }

    private fun dialogReja(p: Programacion) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(context).inflate(R.layout.dialog_reja, null)
        val linearLayoutLoad: ConstraintLayout = view.findViewById(R.id.linearLayoutLoad)
        val linearLayoutPrincipal: LinearLayout = view.findViewById(R.id.linearLayoutPrincipal)
        val imageViewClose: ImageView = view.findViewById(R.id.imageViewClose)
        val tableView: TableView = view.findViewById(R.id.tableView)

        builder.setView(view)
        val dialog = builder.create()
        dialog.show()

        imageViewClose.setOnClickListener {
            dialog.dismiss()
        }
        itfViewModel.setInfo()
        itfViewModel.setRejas()
        itfViewModel.syncProgramacionReja(p.especialidadId)

        val mTableAdapter =
            TableRejaAdapter(context)
        tableView.adapter = mTableAdapter
//        tableView.tableViewListener = MyTableViewListener(tableView)

        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                itfViewModel.rejas.observe(this, { p ->
                    if (p != null) {
                        mTableAdapter.setUserList(p)
                        tableView.visibility = View.VISIBLE
                        linearLayoutLoad.visibility = View.GONE
                        linearLayoutPrincipal.visibility = View.VISIBLE
                    }
                })
            }, 800)
        }
        itfViewModel.mensajeInfo.observe(viewLifecycleOwner, {
            if (it != null) {
                Util.toastMensaje(requireContext(), it)
                dialog.dismiss()
            }
        })
    }
}