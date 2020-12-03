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
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.*
import com.itfperu.appitf.data.viewModel.ActividadViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.activities.FormActivity
import com.itfperu.appitf.ui.adapters.ActividadAdapter
import com.itfperu.appitf.ui.adapters.ComboCicloAdapter
import com.itfperu.appitf.ui.adapters.ComboEstadoAdapter
import com.itfperu.appitf.ui.adapters.ComboPersonalAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_actividades.*
import kotlinx.android.synthetic.main.fragment_edit_aprobation_a.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ActividadFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabAdd -> startActivity(
                Intent(context, FormActivity::class.java)
                    .putExtra(
                        "title", when (tipoActividad) {
                            1 -> "Nueva Actividad"
                            else -> "Aprobación Actividad"
                        }
                    )
                    .putExtra(
                        "tipo", when (tipoActividad) {
                            1 -> 12
                            else -> 13
                        }
                    )
                    .putExtra("id", 0)
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
    lateinit var itfViewModel: ActividadViewModel
    lateinit var adapter: ActividadAdapter
    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null
    private var usuarioId: Int = 0
    private var tipoActividad: Int = 0 // 1 -> actividades , 2 -> aprobadas
    private var estadoId: Int = 0
    lateinit var f: Filtro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        f = Filtro()

        arguments?.let {
            usuarioId = it.getInt(ARG_PARAM1)
            tipoActividad = it.getInt(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_actividades, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(ActividadViewModel::class.java)

        adapter = ActividadAdapter(object : OnItemClickListener.ActividadListener {
            override fun onItemClick(a: Actividad, view: View, position: Int) {
                if (tipoActividad == 1) {
                    if (a.estado == 8 || a.estado == 9)
                        return
                }
                val title = when (tipoActividad) {
                    1 -> "Modificar Actividad"
                    else -> "APRUEBA O RECHAZA ACTIVIDAD"
                }
                val tipo = when (tipoActividad) {
                    1 -> 12
                    else -> 13
                }
                startActivity(
                    Intent(context, FormActivity::class.java)
                        .putExtra("title", title)
                        .putExtra("tipo", tipo)
                        .putExtra("id", a.actividadId)
                        .putExtra("uId", usuarioId)
                )
            }
        })

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        f = Filtro(
            if (tipoActividad == 1) usuarioId else 0, f.cicloId,
            if (tipoActividad == 1) 0 else 7, tipoActividad
        )

        itfViewModel.setLoading(true)
        itfViewModel.syncActividad(
            if (tipoActividad == 1) usuarioId else 0, f.cicloId,
            if (tipoActividad == 1) 0 else 7, tipoActividad
        )

        itfViewModel.search.value = Gson().toJson(f)

        itfViewModel.getActividades().observe(viewLifecycleOwner, {
            textviewMessage.text = String.format("Se encontraron %s registros", it.size)
//            refreshLayout.isRefreshing = false
            adapter.addItems(it)
        })

        if (tipoActividad == 2) {
            fabSave.title = "Enviar Aprobaciones"
            fabAdd.visibility = View.GONE
        }
        itfViewModel.mensajeError.observe(viewLifecycleOwner, {
            closeLoad()
            Util.toastMensaje(context!!, it)
        })
        itfViewModel.mensajeSuccess.observe(viewLifecycleOwner, {
            closeLoad()
            Util.toastMensaje(context!!, it)
        })
        itfViewModel.loading.observe(viewLifecycleOwner, {
            if (it) {
                adapter.addItems(listOfNotNull())
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })


//        refreshLayout.setOnRefreshListener(this)
        fabAdd.setOnClickListener(this)
        fabSave.setOnClickListener(this)
    }

    private fun load() {
        builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(context).inflate(R.layout.dialog_login, null)
        builder.setView(view)
        val textViewTitle: TextView = view.findViewById(R.id.textView)
        textViewTitle.text = String.format("Actualizando..")
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
            ActividadFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }

    private fun dialogSearch() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_filtro_actividad, null)

        val editTextCiclo: TextInputEditText = v.findViewById(R.id.editTextCiclo)
        val editTextUsuario: TextInputEditText = v.findViewById(R.id.editTextUsuario)
        val editTextEstado: TextInputEditText = v.findViewById(R.id.editTextEstado)

        val layoutU: TextView = v.findViewById(R.id.layoutU)
        val layoutUsuario: TextInputLayout = v.findViewById(R.id.layoutUsuario)
        val fabSearch: ExtendedFloatingActionButton = v.findViewById(R.id.fabSearch)


        if (tipoActividad == 2) {
            layoutU.visibility = View.VISIBLE
            layoutUsuario.visibility = View.VISIBLE
        }

        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        f = Filtro()

        editTextCiclo.setOnClickListener { spinnerDialog(1, "Ciclo", editTextCiclo, f) }
        editTextUsuario.setOnClickListener { spinnerDialog(2, "Usuarios", editTextUsuario, f) }
        editTextEstado.setOnClickListener { spinnerDialog(3, "Estado", editTextEstado, f) }
        fabSearch.setOnClickListener {
            if (f.cicloId == 0) {
                itfViewModel.setError("Seleccione Ciclo")
                return@setOnClickListener
            }
            if (f.estadoId == 0) {
                itfViewModel.setError("Seleccione Estado")
                return@setOnClickListener
            }

            if (tipoActividad == 2) {
                if (f.usuarioId == 0) {
                    itfViewModel.setError("Seleccione Usuario")
                    return@setOnClickListener
                }
            }

            itfViewModel.setLoading(true)
            itfViewModel.syncActividad(
                if (f.usuarioId == 0) usuarioId else f.usuarioId,
                f.cicloId,
                f.estadoId,
                tipoActividad
            )
            val search = Filtro(
                if (f.usuarioId == 0) usuarioId else f.usuarioId,
                f.cicloId,
                f.estadoId,
                tipoActividad
            )
            itfViewModel.search.value = Gson().toJson(search)

            dialog.dismiss()
        }
    }

    private fun spinnerDialog(tipo: Int, title: String, input: TextInputEditText, f: Filtro) {
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
            1 -> {
                val cicloAdapter = ComboCicloAdapter(object : OnItemClickListener.CicloListener {
                    override fun onItemClick(c: Ciclo, view: View, position: Int) {
                        f.cicloId = c.cicloId
                        input.setText(c.nombre)
                        dialog.dismiss()
                    }
                })
                recyclerView.adapter = cicloAdapter
                itfViewModel.getCiclos().observe(viewLifecycleOwner, {
                    if (it.isNullOrEmpty()) {
                        itfViewModel.setError("Datos vacios favor de sincronizar.")
                    }
                    cicloAdapter.addItems(it)
                })
            }
            2 -> {
                val personalAdapter =
                    ComboPersonalAdapter(object : OnItemClickListener.PersonalListener {
                        override fun onItemClick(p: Personal, view: View, position: Int) {
                            f.usuarioId = p.personalId
                            input.setText(
                                String.format(
                                    "%s %s %s",
                                    p.nombre, p.apellidoP, p.apellidoM
                                )
                            )
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = personalAdapter
                itfViewModel.getUsuarios().observe(this, {
                    personalAdapter.addItems(it)
                })
            }
            3 -> {
                val estadoAdapter = ComboEstadoAdapter(object : OnItemClickListener.EstadoListener {
                    override fun onItemClick(e: Estado, view: View, position: Int) {
                        f.estadoId = e.estadoId
                        input.setText(e.descripcion)
                        dialog.dismiss()
                    }
                })
                recyclerView.adapter = estadoAdapter
                itfViewModel.getEstados("tbl_Actividades").observe(viewLifecycleOwner, {
                    if (it.isNullOrEmpty()) {
                        itfViewModel.setError("Datos vacios favor de sincronizar.")
                    }
                    estadoAdapter.addItems(it)
                })
            }
        }
    }

    private fun confirmSend() {
        val dialog = MaterialAlertDialogBuilder(context!!)
            .setTitle("Mensaje")
            .setMessage(String.format("Deseas enviar las solicitudes ?."))
            .setPositiveButton("Si") { dialog, _ ->
                load()
                itfViewModel.sendActividad(tipoActividad)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
    }
}