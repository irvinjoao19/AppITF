package com.itfperu.appitf.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Filtro
import com.itfperu.appitf.data.local.model.PuntoContacto
import com.itfperu.appitf.data.viewModel.PuntoContactoViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Gps
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.adapters.PuntoContactoAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_direcciones.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PuntoContactoFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        confirmSend()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: PuntoContactoViewModel
    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null
    private var usuarioId: Int = 0
    private var finicio: String = Util.getFirstDay()
    private var ffinal: String = Util.getLastaDay()
    lateinit var f: Filtro

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
        }
        f = Filtro(
            usuarioId, Util.getFecha(), "", 0, 1
        )

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_punto_contacto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(PuntoContactoViewModel::class.java)

        val puntoContactoAdapter =
            PuntoContactoAdapter(object : OnItemClickListener.PuntoContactoListener {
                override fun onItemClick(p: PuntoContacto, view: View, position: Int) {
                    if (p.estadoId == 30){
                        confirmSave(p)
                    }
                }
            })
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = puntoContactoAdapter

        itfViewModel.setLoading(true)
        itfViewModel.syncPuntoContacto(usuarioId, finicio, ffinal)
        itfViewModel.search.value = Gson().toJson(f)
        itfViewModel.getPuntoContactos().observe(viewLifecycleOwner, {
            textviewMessage.text = String.format("Se encontraron %s registros", it.size)
            puntoContactoAdapter.addItems(it)
        })
        itfViewModel.loading.observe(viewLifecycleOwner, {
            if (it) {
                puntoContactoAdapter.addItems(listOfNotNull())
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
        fabSave.setOnClickListener(this)
    }

    private fun dialogSearch() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_filtro_punto_contacto, null)

        val editTextFecha: TextInputEditText = v.findViewById(R.id.editTextFecha)
        val fabSearch: ExtendedFloatingActionButton = v.findViewById(R.id.fabSearch)

        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        f = Filtro()

        editTextFecha.setOnClickListener { Util.getDateDialog(requireContext(), editTextFecha) }
        fabSearch.setOnClickListener {
            val fecha = editTextFecha.text.toString()
            val search = Filtro(
                usuarioId, fecha, f.ffinal, f.estadoId, 1
            )
            itfViewModel.search.value = Gson().toJson(search)
            dialog.dismiss()
        }
    }

    private fun confirmSend() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Mensaje")
            .setMessage(String.format("Deseas enviar las solicitudes ?."))
            .setPositiveButton("Si") { dialog, _ ->
                load()
                itfViewModel.sendPuntoContacto()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
    }

    private fun confirmSave(p: PuntoContacto) {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Mensaje")
            .setMessage(String.format("Desea registrar el punto de contacto?."))
            .setPositiveButton("Si") { dialog, _ ->
                val gps = Gps(requireContext())
                if (gps.isLocationEnabled()) {
                    if (gps.latitude.toString() != "0.0" || gps.longitude.toString() != "0.0") {
                        p.latitud = gps.latitude.toString()
                        p.longitud = gps.longitude.toString()
                        p.estadoId = 31
                        p.descripcionEstado = "Registrado"
                        p.active = 0
                        load()
                        itfViewModel.sendOnlinePuntoContacto(p)
                    }
                } else {
                    gps.showSettingsAlert(requireContext())
                }
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

    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            PuntoContactoFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }
}